package com.android.priyanka.firebasecrud;

public class Person {

    String personId,personName,personQualification;


    public Person() {

    }


    public Person(String personId, String personName, String personQualification) {
        this.personId = personId;
        this.personName = personName;
        this.personQualification = personQualification;
    }

    public String getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }

    public String getPersonQualification() {
        return personQualification;
    }
}
