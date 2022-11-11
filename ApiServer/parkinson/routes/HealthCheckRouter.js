const express = require('express');
const router = express.Router();

router.get("/", (request, response, next) => {
    return response.sendStatus(200);
});

module.exports = router;