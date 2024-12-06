const jwt = require('jsonwebtoken');
const { v4: uuidv4 } = require('uuid');
const TokenModel = require('../models/token.model');
const errorMessage = require('../errormessage/error.message');

module.exports = {
    generateToken: async (user, remember = false) => {
        if (!user || !user.id) {
            throw new Error(errorMessage.INVALID_USER);
        }

        const jti = uuidv4();
        const expiresIn = remember ? '7d' : '1h';
        
        const token = jwt.sign({
            iss: "study-share-auth-server",
            sub: user.id,
            jti: jti,
            email: user.email,
            name: user.name,
        }, process.env.JWT_SECRET, { expiresIn });

        const expiresAt = new Date(Date.now() + (remember ? 7 * 24 * 60 * 60 * 1000 : 60 * 60 * 1000));

        try {
            await TokenModel.addToWhitelist(user.id, jti, expiresAt);
        } catch (error) {
            throw new Error(errorMessage.TOKEN_GENERATION_FAILED);
        }

        return token;
    },

    checkAll: async (token, userId) => {
        if (!token || !userId) {
            throw new Error(errorMessage.INVALID_TOKEN);
        }

        try {
            const decoded = jwt.verify(token, process.env.JWT_SECRET);
            
            if (decoded.sub !== userId) {
                throw new Error(errorMessage.INVALID_AUTHORITY);
            }

            const isValid = await TokenModel.isValidToken(userId, decoded.jti);
            if (!isValid) {
                throw new Error(errorMessage.INVALID_TOKEN);
            }

            return decoded;
        } catch (error) {
            if (error.name === 'TokenExpiredError') {
                throw new Error(errorMessage.EXPIRED_TOKEN);
            }
            if (error.name === 'JsonWebTokenError') {
                throw new Error(errorMessage.INVALID_TOKEN);
            }
            throw error;
        }
    }
};