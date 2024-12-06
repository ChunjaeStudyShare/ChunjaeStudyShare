const express = require('express');
const router = express.Router();
const UserController = require('../controllers/user.controller');

const userController = new UserController();

router.post('/register', 
    validateRequest(validators.register), 
    (req, res, next) => userController.register(req, res, next)
);
router.put('/update-user', 
    validateRequest(validators.updateUser), 
    (req, res, next) => userController.updateUser(req, res, next)
);
router.put('/update-password', 
    validateRequest(validators.updatePassword), 
    (req, res, next) => userController.updatePassword(req, res, next)
);
router.delete('/delete-user', (req, res, next) => userController.deleteUser(req, res, next));

module.exports = router;
