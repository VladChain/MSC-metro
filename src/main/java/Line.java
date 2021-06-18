public class Line {

    private String number;
    private String name;

    public Line(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber(){
        return number;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

