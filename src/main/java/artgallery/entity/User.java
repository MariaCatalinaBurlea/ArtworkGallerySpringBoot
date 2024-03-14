package artgallery.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@NamedQuery(name = "User.findUserByEmail", query = "select u from User u where u.email=:email")
@NamedQuery(name = "User.getAllUsers", query = "select new " +
        "artgallery.wrapper.UserWrapper(u.id, u.firstName, u.lastName,u.contactNumber,u.email, u.status, u.address) " +
        "from User u where u.role='user'")
@NamedQuery(name = "User.getAllAdmins", query = "select u.email from User u where u.role='admin'")
@NamedQuery(name = "User.updateStatus", query = "update User u set u.status =:status where u.id=:id")

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

    public User() {
    }

    public User(String firstName, String lastName, String contactNumber, String email, String password, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
