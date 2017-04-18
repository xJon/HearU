package xjon.hearu.core;

public class Contact {

    private long contactId, contactAlarmId;
    private String name;
    private String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
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

    public long getContactAlarmId() {
        return contactAlarmId;
    }

    public void setContactAlarmId(long contactAlarmId) {
        this.contactAlarmId = contactAlarmId;
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
