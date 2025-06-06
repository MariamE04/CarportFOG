    package app.entities;

    //User-klassen er en simpel Java-klasse, der repræsenterer en bruger.

    public class User {
        private int id;
        private String email;
        private String password;
        private long PhoneNumber;
        private String role = "customer";

        public User(int id, String email, String password, long phoneNumber, String role) {
            this.id = id;
            this.email = email;
            this.password = password;
            PhoneNumber = phoneNumber;
        }

        public User(String email, String password, String role) {
            this.email = email;
            this.password = password;
            this.role = role;
        }

        public User(String email, String password, long phoneNumber) {
            this.email = email;
            this.password = password;
            PhoneNumber = phoneNumber;
        }

        public User(String email, String password) {
            this.email = email;
            this.password = password;
        }

    public User(int id, String email) {
            this.id = id;
            this.email = email;
    }

        public User(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public long getPhoneNumber() {
            return PhoneNumber;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", PhoneNumber=" + PhoneNumber +
                    ", role='" + role + '\'' +
                    '}';
        }
    }
