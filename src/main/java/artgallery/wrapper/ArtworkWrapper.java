package artgallery.wrapper;

public class ArtworkWrapper {
    private int id;
    private String title;
    private String description;
    private int price;
    private String status;
    private String size;
    private int categoryId;
    private String categoryName;

    public ArtworkWrapper() {
    }

    public ArtworkWrapper(int id, String title, String description, int price, String status, int categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public ArtworkWrapper(int id, String title, String description, int price, String size, String status, int categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.size = size;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
