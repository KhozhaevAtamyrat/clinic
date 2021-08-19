package com.example.demo;

public enum Specialty {
    therapist("терапевт"),
    cardiologist("кардиолог"),
    surgeon("хирург"),
    ophthalmologist("офтальмолог"),
    neurologist("невролог"),
    psychotherapist("психотерапевт"),
    dentist("стоматолог"),
    lore("лор"),
    gastroenterologist("гастроэнтеролог"),
    gynecologist("гинеколог"),
    urologist("уролог");

    private final String name;

    Specialty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Specialty findByName(String name){
        for(Specialty specialty : values()){
            if(specialty.getName().equals(name)){
                return specialty;
            }
        }
        return null;
    }
}
