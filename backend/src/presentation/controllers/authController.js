const authService = require('../../application/services/authService');
const RegisterRequestDto = require('../../application/dtos/authDto/registerRequestDto');
const LoginRequestDto = require('../../application/dtos/authDto/loginRequestDto');
const UpdateProfileRequestDto = require('../../application/dtos/authDto/updateProfileRequestDto');
const ChangePasswordRequestDto = require('../../application/dtos/authDto/changePasswordRequestDto');
const DeleteAccountRequestDto = require('../../application/dtos/authDto/deleteAccountRequestDto');
class AuthController {      
    async register(req, res) { 
        try {
            const registerDto = new RegisterRequestDto(req.body);
            registerDto.validate(); 

            const result = await authService.register(registerDto);
            
            return res.status(201).json({ success: true, message: "Đăng ký thành công", data: result });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }

    async login(req, res) {
        try {
            const loginDto = new LoginRequestDto(req.body);
            loginDto.validate();
            
            const result = await authService.login(loginDto);

            return res.status(200).json({ success: true, message: "Đăng nhập thành công", data: result });
        } catch (error) {
            return res.status(401).json({ success: false, message: error.message });
        }
    }

    async logout(req, res) {
        try {
            const userId = req.user.userId;

            console.log(`[LOGOUT] User ID: ${userId} vừa yêu cầu đăng xuất.`);

            return res.status(200).json({ 
                success: true, 
                message: "Đăng xuất thành công. Vui lòng xóa Token ở thiết bị!" 
            });
        } catch (error) {
            return res.status(500).json({ 
                success: false, 
                message: "Lỗi Server khi đăng xuất" 
            });
        }
    }
    
    async getMyProfile(req, res) {
        try {
            const userId = req.user.userId;

            const profileData = await authService.getProfile(userId);

            return res.status(200).json({
                success: true,
                message: "Lấy thông tin Profile thành công",
                data: profileData
            });
        } catch (error) {
            return res.status(404).json({
                success: false,
                message: error.message
            });
        }
    }

    async updateMyProfile(req, res) {
        try {
            const userId = req.user.userId; 
            
            const updateDto = new UpdateProfileRequestDto(req.body);
            updateDto.validate();
            
            const updatedProfile = await authService.updateProfile(userId, updateDto);

            return res.status(200).json({ success: true, message: "Cập nhật hồ sơ thành công", data: updatedProfile });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }

    async changeMyPassword(req, res) {
        try {
            const userId = req.user.userId;
            
            const changePassDto = new ChangePasswordRequestDto(req.body);
            changePassDto.validate();

            await authService.changePassword(userId, changePassDto);

            return res.status(200).json({ success: true, message: "Đổi mật khẩu thành công!" });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }

    async deleteMyAccount(req, res) {
        try {
            const userId = req.user.userId;
            
            const deleteDto = new DeleteAccountRequestDto(req.body);
            deleteDto.validate();

            await authService.deleteMyAccount(userId, deleteDto.password);

            return res.status(200).json({ 
                success: true, 
                message: "Tài khoản của bạn đã được xóa vĩnh viễn!" 
            });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }
}
module.exports = new AuthController();