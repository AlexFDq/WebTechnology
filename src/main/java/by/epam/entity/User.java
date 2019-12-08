package by.epam.entity;

import java.util.List;
import java.util.Objects;

public class User {
    private String name;
    private String passwordHash;
    private int authorityLevel;
    private List<Integer> cardIds;

    public String getName() {
        return name;
    }

    public List<Integer> getCardIds() {
        return cardIds;
    }

    public void setCardIds(List<Integer> cardIds) {
        this.cardIds = cardIds;
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

    public int getAuthorityLevel() {
        return authorityLevel;
    }

    public void setAuthorityLevel(int authorityLevel) {
        this.authorityLevel = authorityLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return authorityLevel == user.authorityLevel &&
                Objects.equals(name, user.name) &&
                Objects.equals(cardIds, user.cardIds) &&
                Objects.equals(passwordHash, user.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cardIds, passwordHash, authorityLevel);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", cardIds=" + cardIds +
                ", passwordHash='" + passwordHash + '\'' +
                ", authorityLevel=" + authorityLevel +
                '}';
    }
}
