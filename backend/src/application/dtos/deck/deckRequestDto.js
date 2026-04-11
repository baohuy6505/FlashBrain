class deckRequestDto{
    constructor(data) {
        this.title = data.title;
        this.isPublic = data.isPublic || false;
        this.userId = data.userId;
    }

    async validate() {
        if(!this.title || !this.title.trim().length > 0 || typeof this.title !== 'string') {
            throw new Error("Tên bộ thẻ là bắt buộc và phải là một chuỗi");
        }
        if(typeof this.isPublic !== 'boolean') {
            throw new Error("isPublic phải là một giá trị boolean");
        }
        if(!this.userId || typeof this.userId !== 'string') {
            throw new Error("ID người dùng là bắt buộc và phải là một chuỗi");
        }
    }

    
}

module.exports = deckRequestDto;
