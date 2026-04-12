const IAuthService = require('../interfaces/IAuthService');
const bcrypt = require('bcryptjs');
const { v4: uuidv4 } = require('uuid');
const userRepository = require('../../infrastructure/repositories/userRepository');
const UserProfileResponseDto = require('../../application/dtos/authDto/userProfileResponseDto');
const { generateToken } = require('../utils/jwt');
const cloudinary = require('cloudinary').v2;

const getPublicIdFromUrl = (url) => {
    try {
        const parts = url.split('/');
        const uploadIndex = parts.findIndex(part => part === 'upload');
        const publicIdWithExtension = parts.slice(uploadIndex + 2).join('/');
        return publicIdWithExtension.split('.')[0];
    } catch (error) {
        console.error("Không thể tách Public ID từ URL:", error);
        return null;
    }
};

class AuthService extends IAuthService {
    async register(registerDto) {
        
        const { email, password, name } = registerDto;

        const existingUser = await userRepository.findByEmail(email);
        if (existingUser) {
            throw new Error('Email đã được sử dụng');
        }

        const passwordHash = await bcrypt.hash(password, 10);

        const newUser = await userRepository.create({
            _id: uuidv4(),
            name,   
            email,
            passwordHash: passwordHash
        });

        const token = generateToken({ 
            userId: newUser._id, 
            role: newUser.role 
        });

        return {
            token
        };
    }

    async login(loginDto) {

        const { email, password } = loginDto;

        const user = await userRepository.findByEmail(email);
        if (!user) {
            throw new Error('Email hoặc mật khẩu không chính xác');
        }

        const isMatch = await bcrypt.compare(password, user.passwordHash);
        if (!isMatch) {
            throw new Error('Email hoặc mật khẩu không chính xác');
        }

        const token = generateToken({ 
            userId: user._id, 
            role: user.role 
        });

        return {
            token
        };
    }
    
    async logout() {            
        return;
    }

    async getProfile(userId) {

        const user = await userRepository.findById(userId);
        if (!user) {
            throw new Error("Người dùng không tồn tại hoặc đã bị khóa.");
        }

        return new UserProfileResponseDto(user);
    }

    async updateProfile(userId, updateDto) {
        const { name, image } = updateDto;

        const user = await userRepository.findById(userId);
        if (!user) {
            throw new Error("Người dùng không tồn tại hoặc đã bị khóa.");
        }
        
        const oldImage = user.image; // Lưu lại link cũ

        const updatedUser = await userRepository.updateUser(userId, { name, image });
        if (!updatedUser) {
            throw new Error("Không tìm thấy người dùng để cập nhật");
        }

        if (image && oldImage && image !== oldImage) {
            const publicId = getPublicIdFromUrl(oldImage);
            
            if(publicId) {
               try {
                    await cloudinary.uploader.destroy(publicId);
                    console.log(`Đã xóa ảnh cũ thành công: ${publicId}`);
                } catch (err) {
                    console.error("Lỗi xóa ảnh Cloudinary:", err.message);
                }
            }
        }

        return new UserProfileResponseDto(updatedUser);
    }

    async changePassword(userId, changePassDto) {

        const { oldPassword, newPassword } = changePassDto;

        const user = await userRepository.findById(userId);
        if (!user) throw new Error("Không tìm thấy người dùng");

        const isMatch = await bcrypt.compare(oldPassword, user.passwordHash);
        if (!isMatch) {
            throw new Error("Mật khẩu cũ không chính xác!");
        }

        const newPasswordHash = await bcrypt.hash(newPassword, 10);

        await userRepository.updateUser(userId, { passwordHash: newPasswordHash });

        return true; 
    }

    async deleteMyAccount(userId, password) {

        const user = await userRepository.findById(userId);
        if (!user) throw new Error("Không tìm thấy người dùng");

        const isMatch = await bcrypt.compare(password, user.passwordHash);
        if (!isMatch) {
            throw new Error("Mật khẩu xác nhận không chính xác. Hủy thao tác xóa!");
        }

        await userRepository.deleteUser(userId);

        return true;
    }

}
module.exports = new AuthService();