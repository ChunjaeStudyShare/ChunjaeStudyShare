const jwt = require('jsonwebtoken');
const TokenModel = require('../models/token.model');

exports.verifyToken = async (req, res, next) => {
    try {
        const token = req.headers.authorization?.split(' ')[1];
        if (!token) {
            return res.status(401).json({
                success: false,
                message: '인증 토큰이 필요합니다.'
            });
        }

        // JWT 검증
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        
        // 토큰 유효성 검사 (DB에 저장된 토큰인지 확인)
        const isValidToken = await TokenModel.verifyToken(decoded.id, decoded.jti);
        if (!isValidToken) {
            return res.status(401).json({
                success: false,
                message: '유효하지 않은 토큰입니다.'
            });
        }

        // req 객체에 사용자 정보 추가
        req.user = decoded;
        next();
    } catch (error) {
        res.status(401).json({
            success: false,
            message: '인증에 실패했습니다.'
        });
    }
};