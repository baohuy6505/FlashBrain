class UserResponseDto {
    constructor(user) {
        this.id = user._id;
        this.email = user.email;
        this.name = user.name;
        this.image = user.image;
        this.role = user.role;
        this.subscriptionType = user.subscriptionType;
        this.createdAt = user.created_at;
    }
}

module.exports = UserResponseDto;  