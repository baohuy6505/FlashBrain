const IAuthService = require('../interfaces/IAuthService');
const bcrypt = require('bcryptjs');
const { v4: uuidv4 } = require('uuid');
const userRepository = require('../../infrastructure/repositories/userRepository');
const UserProfileResponseDto = require('../../application/dtos/authDto/userProfileResponseDto');
const { generateToken } = require('../utils/jwt');
const cloudinary = require('cloudinary').v2;
const { OAuth2Client } = require('google-auth-library');
const crypto = require('crypto');

    const googleClient = new OAuth2Client(process.env.GOOGLE_CLIENT_ID);

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
  

    async googleLogin(idToken) {
        if (!idToken) {
            throw new Error("Vui lòng cung cấp Google ID Token");
        }

        try {
            const ticket = await googleClient.verifyIdToken({
                idToken: idToken,
                audience: process.env.GOOGLE_CLIENT_ID,  
                // Lưu ý: audience phải khớp với cái GOOGLE_CLIENT_ID trong .env
            });

            const payload = ticket.getPayload();
            const { email, name, picture } = payload;

            let user = await userRepository.findByEmail(email);

            if (!user) {
                const randomPassword = crypto.randomBytes(16).toString('hex');
                const passwordHash = await bcrypt.hash(randomPassword, 10);

                user = await userRepository.create({
                    _id: uuidv4(),
                    email: email,
                    name: name,
                    image: picture,
                    passwordHash: passwordHash
                });
            } else {
                if ((!user.image || user.image === "") && picture) {
                    await userRepository.updateUser(user._id, { image: picture });
                    user.image = picture;
                }
            }

            const token = generateToken({ 
                userId: user._id, 
                role: user.role 
            });

            return { token, isNewUser: !user }

        } catch (error) {
            console.error("Lỗi xác thực Google:", error.message);
            throw new Error("Token Google không hợp lệ hoặc đã hết hạn!");
        }
    }

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