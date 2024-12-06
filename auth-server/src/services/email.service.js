const nodemailer = require('nodemailer');
const crypto = require('crypto');
const EmailCodeModel = require('../models/emailcode.model');

class EmailService {
    constructor() {
        this.transporter = nodemailer.createTransport({
            host: process.env.EMAIL_HOST,
            port: process.env.EMAIL_PORT,
            secure: false,  // TLS
            auth: {
                user: process.env.EMAIL_USER,
                pass: process.env.EMAIL_PASSWORD
            },
            tls: {
                rejectUnauthorized: false  // 자체 서명 인증서 허용
            }
        });
    }

    // 인증 토큰 생성
    generateVerificationToken() {
        return crypto.randomBytes(32).toString('hex');
    }

    // 인증 메일 발송
    async sendVerificationEmail(userId, email) {
        const token = this.generateVerificationToken();
        const verificationLink = `https://api.gyeongminiya.asia/api/user/verify-email?token=${token}&userId=${userId}`;

        const mailOptions = {
            from: process.env.EMAIL_USER,
            to: email,
            subject: '[StudyShare] 이메일 인증',
            html: `
                <div style="max-width: 600px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif;">
                    <h2 style="color: #333; text-align: center;">StudyShare 이메일 인증</h2>
                    <p style="color: #666;">안녕하세요!</p>
                    <p style="color: #666;">StudyShare 회원가입을 위한 이메일 인증을 진행해주세요.</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="${verificationLink}" 
                           style="background-color: #4CAF50; 
                                  color: white; 
                                  padding: 12px 24px; 
                                  text-decoration: none; 
                                  border-radius: 4px;
                                  display: inline-block;">
                            이메일 인증하기
                        </a>
                    </div>
                    <p style="color: #666; font-size: 14px;">
                        이 링크는 24시간 동안 유효합니다.<br>
                        본인이 요청하지 않은 경우 이 메일을 무시해주세요.
                    </p>
                    <div style="margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; text-align: center;">
                        <p style="color: #999; font-size: 12px;"><br>
                            © 2024 StudyShare. All rights reserved.
                        </p>
                    </div>
                </div>
            `
        };

        try {
            await this.transporter.sendMail(mailOptions);
            await EmailCodeModel.create(userId, token);
            return true;
        } catch (error) {
            console.error('Email sending failed:', error);
            throw new Error('이메일 전송에 실패했습니다.');
        }
    }

    // 비밀번호 재설정 메일 발송 (추가 기능으로 구현 가능)
    // 비밀번호 재설정 페이지는 스프링 부트 서버에서 구현
    async sendPasswordResetEmail(userId, email) {
        const token = this.generateVerificationToken();
        const resetLink = `https://www.gyeongminiya.asia/reset-password?token=${token}&userId=${userId}`;

        const mailOptions = {
            from: process.env.EMAIL_USER,
            to: email,
            subject: '[StudyShare] 비밀번호 재설정',
            html: `
                <div style="max-width: 600px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif;">
                    <h2 style="color: #333; text-align: center;">비밀번호 재설정</h2>
                    <p style="color: #666;">비밀번호 재설정을 요청하셨습니다.</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="${resetLink}" 
                           style="background-color: #4CAF50; 
                                  color: white; 
                                  padding: 12px 24px; 
                                  text-decoration: none; 
                                  border-radius: 4px;
                                  display: inline-block;">
                            비밀번호 재설정하기
                        </a>
                    </div>
                    <p style="color: #666; font-size: 14px;">
                        이 링크는 1시간 동안 유효합니다.<br>
                        본인이 요청하지 않은 경우 이 메일을 무시해주세요.
                    </p>
                </div>
            `
        };

        try {
            await this.transporter.sendMail(mailOptions);
            await EmailCodeModel.create(userId, token);
            return true;
        } catch (error) {
            console.error('Email sending failed:', error);
            throw new Error('이메일 전송에 실패했습니다.');
        }
    }
}

module.exports = new EmailService();