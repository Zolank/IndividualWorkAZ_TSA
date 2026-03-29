package csv_parsing;

/**
 * Класс, представляющий модель данных студента.
 */
public class Student {
    private int id;
    private String name;
    private int age;
    private double grade;

    /**
     * Конструктор для создания объекта студента.
     *
     * @param id    уникальный идентификатор
     * @param name  имя студента
     * @param age   возраст
     * @param grade средний балл (оценка)
     */
    public Student(int id, String name, int age, double grade) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", grade=" + grade +
                '}';
    }
}