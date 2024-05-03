package artgallery.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@NamedQuery(name = "Artwork.getAllArtworks", query = "select new  artgallery.wrapper.ArtworkWrapper(a.id, a.title, a.description, a.price, a.size, a.status," +
        " a.category.id, a.category.name) from Artwork a")

@NamedQuery(name = "Artwork.getArtworkById", query = "select new  artgallery.wrapper.ArtworkWrapper(a.id, a.title, a.description, a.price, a.size, a.status," +
        " a.category.id, a.category.name) from Artwork a where a.id=:id")

@NamedQuery(name = "Artwork.getArtworksByCategoryId", query = "select new  artgallery.wrapper.ArtworkWrapper(a.id, a.title, a.description, a.price, a.status," +
        " a.category.id, a.category.name) from Artwork a where a.category.id=:id")

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "artwork")
public class Artwork implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "size")
    private String size;

    @Column(name = "price")
    private int price;

    @Column(name = "status")
    private String status;

    public Artwork() {
    }

    public Artwork(int id, String title, Category category, String description, String size, int price, String status) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.size = size;
        this.price = price;
        this.status = status;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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
}
