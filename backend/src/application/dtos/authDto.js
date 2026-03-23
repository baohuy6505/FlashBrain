class UserResponseDto {
    constructor(user) {
        this.id = user._id;
        this.email = user.email;
        this.name = user.name;
        this.role = user.role;
        this.subscriptionType = user.subscription_type;
        this.createdAt = user.created_at;
    }
}

module.exports = UserResponseDto;