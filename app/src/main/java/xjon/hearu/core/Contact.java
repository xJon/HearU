package xjon.hearu.core;

public class Contact {

    private long contactId;
    private int contactAlarmId;
    private String name, number;

    public Contact(String name, String number, int contactAlarmId) {
        this.name = name;
        this.number = number;
        this.contactAlarmId = contactAlarmId;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public int getContactAlarmId() {
        return contactAlarmId;
    }

    public void setContactAlarmId(int contactAlarmId) {
        this.contactAlarmId = contactAlarmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", contactAlarmId=" + contactAlarmId +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
