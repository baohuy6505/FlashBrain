const IUserRepository = require('../interfaces/IUserRepository');
const User = require('../../domain/models/User');

class UserRepository extends IUserRepository {
    async create(userData) {
        const user = new User({
            _id: userData._id,
            email: userData.email,
            name: userData.name,
            passwordHash: userData.passwordHash,
            role: userData.role || 'USER',
            subscriptionType: userData.subscriptionType || 'FREE',
        });
        return await user.save();
    }
    
    async findByEmail(email) {
        return await User.findOne({ email: email, isDeleted: false });
    }

    async findById(userId) {
        return await User.findOne({ _id: userId, isDeleted: false });
    }

    async updateUser(userId, userUpdate) {
        const updateData = {};

        if (userUpdate.name !== undefined) updateData.name = userUpdate.name;
        if (userUpdate.image !== undefined) updateData.image = userUpdate.image;
        if (userUpdate.passwordHash !== undefined) updateData.passwordHash = userUpdate.passwordHash;
        if (userUpdate.subscriptionType !== undefined) updateData.subscriptionType = userUpdate.subscriptionType;
        
        return await User.findOneAndUpdate(
            { _id: userId, isDeleted: false },
            { $set: updateData },
            { new: true }
        );
    }

    async deleteUser(userId) {
        return await User.findOneAndDelete({ _id: userId });
    }
}

module.exports = new UserRepository();