const express = require('express');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;
const API_TARGET = 'http://localhost:8080';

// Parse JSON bodies for non-GET requests
app.use(express.json({ limit: '1mb' }));

const proxyPaths = [
	'/adduser', '/addBio', '/deleteBio', '/changeBio', '/addStatus', '/deleteStatus',
	'/changeUserName', '/changeName', '/follow', '/allusers', '/unfollow',
	'/getAllFollowers', '/getAllFollowing', '/blockuser', '/unBlockuser',
	'/uploadPost', '/delete', '/allpost'
];

// Manual forwarder using built-in fetch (Node 18+)
async function forwardRequest(req, res) {
	try {
		const url = new URL(req.originalUrl, API_TARGET);
		const method = (req.method || 'GET').toUpperCase();
		const headers = { Accept: 'application/json, text/plain;q=0.9, */*;q=0.8' };
		// Copy over headers except hop-by-hop
		for (const [k, v] of Object.entries(req.headers)) {
			if (!['host', 'content-length'].includes(k.toLowerCase())) headers[k] = v;
		}
		if (method === 'GET' || method === 'HEAD') {
			delete headers['content-type'];
		}
		const init = { method, headers };
		if (!(method === 'GET' || method === 'HEAD')) {
			init.body = req.body ? JSON.stringify(req.body) : undefined;
		}
		const upstream = await fetch(url, init);
		res.status(upstream.status);
		upstream.headers.forEach((value, key) => {
			if (!['content-encoding', 'transfer-encoding'].includes(key)) {
				res.setHeader(key, value);
			}
		});
		const buf = Buffer.from(await upstream.arrayBuffer());
		return res.send(buf);
	} catch (err) {
		console.error('Forward error', req.method, req.originalUrl, err.message);
		return res.status(502).send('Bad gateway');
	}
}

// Register forwarders before static
proxyPaths.forEach((route) => {
	app.all(route + '*', forwardRequest);
});

// Static files
app.use(express.static(path.join(__dirname, 'public')));

app.get('*', (req, res) => {
	res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.listen(PORT, () => console.log(`Frontend running on http://localhost:${PORT}`));


