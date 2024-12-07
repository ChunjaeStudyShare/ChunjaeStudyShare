const router = require('express').Router();
const AuthController = require('../controllers/auth.controller');
const { verifyToken } = require('../middleware/auth.middleware');

// const authController = new AuthController();

// POST /api/auth/login - 로그인
router.post('/login', AuthController.login);

// POST /api/auth/logout - 로그아웃 (인증 필요)
router.post('/logout', verifyToken, AuthController.logout);

module.exports = router;