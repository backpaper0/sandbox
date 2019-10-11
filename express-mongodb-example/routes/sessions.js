/*

curl -v -c cookie -b cookie localhost:3000/sessions/hello -d value=world

curl -v -c cookie -b cookie localhost:3000/sessions/hello

 */

const express = require('express');
const router = express.Router();

router.get('/:key', async (req, res, next) => {

	const { key } = req.params;
	const value = req.session[key];

	res.send({ key, value });
});

router.post('/:key', async (req, res, next) => {

	const { key } = req.params;
	const value = req.body.value;

	req.session[key] = value;

  res.send({ key, value });
});

module.exports = router;
