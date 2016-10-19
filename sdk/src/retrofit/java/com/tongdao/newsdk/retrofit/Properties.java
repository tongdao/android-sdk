
package com.tongdao.newsdk.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Properties {

    @SerializedName("!email")
    @Expose
    private String email;
    @SerializedName("!first_name")
    @Expose
    private String firstName;
    @SerializedName("!last_name")
    @Expose
    private String lastName;
    @SerializedName("!name")
    @Expose
    private String name;
    @SerializedName("!username")
    @Expose
    private String username;
    @SerializedName("!phone")
    @Expose
    private String phone;
    @SerializedName("!gender")
    @Expose
    private String gender;
    @SerializedName("!age")
    @Expose
    private Integer age;
    @SerializedName("!avatar")
    @Expose
    private String avatar;
    @SerializedName("!address")
    @Expose
    private String address;
    @SerializedName("!birthday")
    @Expose
    private String birthday;
    @SerializedName("!source")
    @Expose
    private Source source;
    @SerializedName("!application")
    @Expose
    private Application application;
    @SerializedName("!connection")
    @Expose
    private Connection connection;
    @SerializedName("!location")
    @Expose
    private Location location;
    @SerializedName("!device")
    @Expose
    private Device device;
    @SerializedName("!fingerprint")
    @Expose
    private Fingerprint fingerprint;

    /**
     *
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     *     The !email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     *     The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     *     The !first_name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     *     The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     *     The !last_name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The !name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     *     The !username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     *     The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     *     The !phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     *     The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     *     The !gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     *     The age
     */
    public Integer getAge() {
        return age;
    }

    /**
     *
     * @param age
     *     The !age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     *
     * @return
     *     The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     *
     * @param avatar
     *     The !avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     *
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     *     The !address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     *     The birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     *
     * @param birthday
     *     The !birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     *
     * @return
     *     The source
     */
    public Source getSource() {
        return source;
    }

    /**
     *
     * @param source
     *     The !source
     */
    public void setSource(Source source) {
        this.source = source;
    }

    /**
     *
     * @return
     *     The application
     */
    public Application getApplication() {
        return application;
    }

    /**
     *
     * @param application
     *     The !application
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     *
     * @return
     *     The connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     *
     * @param connection
     *     The !connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @return
     *     The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     *     The !location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     *
     * @return
     *     The device
     */
    public Device getDevice() {
        return device;
    }

    /**
     *
     * @param device
     *     The !device
     */
    public void setDevice(Device device) {
        this.device = device;
    }

    /**
     * 
     * @return
     *     The fingerprint
     */
    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    /**
     * 
     * @param fingerprint
     *     The !fingerprint
     */
    public void setFingerprint(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
    }

}
