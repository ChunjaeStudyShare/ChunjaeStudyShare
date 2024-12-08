const jwt = require('jsonwebtoken');
const TokenModel = require('../models/token.model');
const errorMessage = require('../errormessage/error.message');

exports.verifyToken = async (req, res, next) => {
    try {
        const token = req.headers.authorization?.split(' ')[1];
        if (!token) {
            return res.status(401).json({
                success: false,
                message: errorMessage.INVALID_TOKEN
            });
        }

        // JWT 검증
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        
        // 토큰 유효성 검사 (DB에 저장된 토큰인지 확인)
        const isValidToken = await TokenModel.verifyToken(decoded.userId, decoded.jti);
        if (!isValidToken) {
            return res.status(401).json({
                success: false,
                message: errorMessage.INVALID_TOKEN
            });
        }

        // req 객체에 사용자 정보 추가
        req.user = decoded;
        next();
    } catch (error) {
        res.status(401).json({
            success: false,
            message: errorMessage.INVALID_TOKEN
        });
    }
};