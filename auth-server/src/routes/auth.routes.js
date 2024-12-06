const express = require('express');
const router = express.Router();
const AuthController = require('../controllers/auth.controller');
const validateRequest = require('../validators/validateRequest');
const validators = require('../validators/validators');

const authController = new AuthController();

router.post('/login', 
    validateRequest(validators.login), 
    (req, res, next) => authController.login(req, res, next)
);

module.exports = router;
