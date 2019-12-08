package by.epam.entity;

import java.util.Objects;

public class Admin {
    private String name;
    private String passwordHash;
    private int authorityLevel;

    public int getAuthorityLevel() {
        return authorityLevel;
    }

    public void setAuthorityLevel(int authorityLevel) {
        this.authorityLevel = authorityLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return authorityLevel == admin.authorityLevel &&
                Objects.equals(name, admin.name) &&
                Objects.equals(passwordHash, admin.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, passwordHash, authorityLevel);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "name='" + name + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", authorityLevel=" + authorityLevel +
                '}';
    }
}
