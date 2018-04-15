var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('./client/Welcome_Page', function(req, res, next) {
    res.render('./client/Welcome_Page.html');
});

//allows access from a different file
module.exports = router;