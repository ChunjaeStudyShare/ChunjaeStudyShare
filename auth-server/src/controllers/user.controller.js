const UserService = require('../services/user.service');

class UserController {
    constructor() {
        this.userService = new UserService();
    }

    async register(req, res, next) {
        try {
            const userData = req.body;
            const result = await this.userService.register(userData);
            
            res.status(201).json({
                success: true,
                message: '회원가입이 완료되었습니다.',
                data: result
            });
        } catch (error) {
            next(error);
        }
    }
    async updateUser(req, res, next) {
        try {
            const userData = req.body;
            const result = await this.userService.updateUser(userData);
            if (!result) {
                return res.status(404).json({
                    success: false,
                    message: '회원정보를 찾을 수 없습니다.',
                });
            }
            //jwt 토큰 재발급
            const token = TokenBuilderUtil.generateToken(result);
            res.status(200).json({
                success: true,
                message: '회원정보 수정이 완료되었습니다.',
                data: result,
                token
            });
        } catch (error) {
            next(error);
        }
    }
    async updatePassword(req, res, next) {
        try {
            const userData = req.body;
            const result = await this.userService.updatePassword(userData);
            res.status(200).json({
                success: true,
                message: '비밀번호 수정이 완료되었습니다.',
                data: result
            });
        } catch (error) {
            next(error);
        }
    }
}

module.exports = new UserController();