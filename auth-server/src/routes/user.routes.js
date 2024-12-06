const express = require('express');
const router = express.Router();
const UserController = require('../controllers/user.controller');
const { verifyToken } = require('../middleware/auth.middleware');
const { validateRequest } = require('../middleware/user.middleware');
const validators = require('../validators/validators');

// Public routes
router.post('/register', 
    validateRequest(validators.register),
    (req, res, next) => UserController.register(req, res, next)
);

// Protected routes (토큰 검증 필요)
router.put('/update-password',
    verifyToken,  // 토큰 검증
    validateRequest(validators.updatePassword),
    (req, res, next) => UserController.updatePassword(req, res, next)
);

router.put('/update-user',
    verifyToken,  // 토큰 검증
    validateRequest(validators.updateUser),
    (req, res, next) => UserController.updateUser(req, res, next)
);

// router.delete('/delete-user',
//     verifyToken,  // 토큰 검증
//     (req, res, next) => UserController.deleteUser(req, res, next)
// );
// 탈퇴는 스프링서버에서 처리

module.exports = router;
