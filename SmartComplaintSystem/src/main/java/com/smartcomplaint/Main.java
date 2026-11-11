package com.smartcomplaint;

import javafx.application.Application;

import javafx.geometry.Insets;

import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.layout.*;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;

import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    private Stage stage;

    // ================= COMMON DEPARTMENTS =================

    private static final String[] DEPARTMENTS = {

            "Electrical",

            "Water",

            "Hostel",

            "Mess",

            "Maintenance",

            "General"

    };

    @Override

    public void start(Stage stage) {

        this.stage = stage;

        Database.initialize();

        stage.setTitle(

                "Smart Complaint & Grievance Redressal System"

        );

        showLogin(stage);

        stage.show();

    }

    // ================= LOGIN =================

    private void showLogin(Stage stage) {

        VBox box = new VBox(15);

        box.setPadding(new Insets(30));

        box.setAlignment(Pos.CENTER);
        box.setStyle(getMainStyle());

        Label title =

                new Label(

                        "Smart Complaint & Grievance Redressal System"

                );

        title.setFont(Font.font(24));

        TextField username =

                new TextField();

        username.setPromptText("Username");
        styleTextField(username);

        PasswordField password =

                new PasswordField();

        password.setPromptText("Password");
        stylePasswordField(password);

        ComboBox<String> role =

                new ComboBox<>();

        role.getItems().addAll(

                "Student",

                "Admin",

                "Worker"

        );

        role.setValue("Student");
        styleComboBox(role);

        ComboBox<String> department =

                new ComboBox<>();

        department.getItems().addAll(DEPARTMENTS);

        department.setPromptText(

                "Select Department"

        );

        department.setVisible(false);

        department.setManaged(false);

        role.setOnAction(e -> {

            boolean isWorker =

                    "Worker".equals(role.getValue());

            department.setVisible(isWorker);

            department.setManaged(isWorker);

        });

        Button login =

                new Button("Login");
        styleButton(login);

        Button register =

                new Button("New User? Register");
        styleButton(register);

        login.setMaxWidth(380);
        register.setMaxWidth(380);
        styleButton(login);
        styleButton(register);

        login.setOnAction(e -> {

            String enteredUsername =

                    username.getText().trim();

            String enteredPassword =

                    password.getText();

            String selectedRole =

                    role.getValue();

            if (enteredUsername.isEmpty()) {

                showAlert(

                        "Please enter username."

                );

                return;

            }

            if (enteredPassword.isEmpty()) {

                showAlert(

                        "Please enter password."

                );

                return;

            }

            if (selectedRole.equals("Worker") &&

                    department.getValue() == null) {

                showAlert(

                        "Please select your department."

                );

                return;

            }

            String selectedDepartment =

                    department.getValue();

            String userName =

                    Database.authenticateUser(

                            enteredUsername,

                            enteredPassword,

                            selectedRole,

                            selectedDepartment

                    );

            if (userName == null) {

                showAlert(

                        "Invalid username, password, role, or department."

                );

                return;

            }

            if (selectedRole.equals("Student")) {

                showStudentDashboard(

                        userName,

                        enteredUsername

                );

            } else if (selectedRole.equals("Admin")) {

                showAdminDashboard();

            } else {

                showWorkerDashboard(

                        userName

                );

            }

        });

        register.setOnAction(

                e -> showRegister()

        );

        box.getChildren().addAll(

                title,

                username,

                password,

                role,

                department,

                login,

                register

        );

        stage.setScene(

                new Scene(box, 900, 600)

        );

    }

    // ================= REGISTRATION =================

    private void showRegister() {

        BorderPane root =

                baseLayout(

                        "User Registration",

                        () -> showLogin(stage)

                );

        VBox form = new VBox(12);

        form.setPadding(new Insets(30));

        form.setMaxWidth(450);
        form.setAlignment(Pos.TOP_CENTER);

        // ================= FULL NAME =================

        Label nameLabel = new Label("Full Name");
        styleLabel(nameLabel);

        TextField name = new TextField();

        name.setPromptText("Enter your full name");
        styleTextField(name);

        // ================= REGISTRATION NUMBER =================

        Label usernameLabel =

                new Label("Registration Number");

        TextField username = new TextField();

        username.setPromptText("Enter 10-digit registration number");
        styleTextField(username);

        // Allow maximum 10 characters

        username.setTextFormatter(

                new TextFormatter<String>(change -> {

                    if (change.getControlNewText().length() > 10) {

                        return null;

                    }

                    // Allow only letters and numbers

                    if (!change.getText().matches("[a-zA-Z0-9]*")) {

                        return null;

                    }

                    return change;

                })

        );

        // ================= PASSWORD =================

        Label passwordLabel = new Label("Password");
        styleLabel(passwordLabel);

        PasswordField password = new PasswordField();

        password.setPromptText("Enter password");
        stylePasswordField(password);

        // ================= CONFIRM PASSWORD =================

        Label confirmPasswordLabel =

                new Label("Confirm Password");

        PasswordField confirmPassword =

                new PasswordField();

        confirmPassword.setPromptText(

                "Re-enter your password"

        );

        // ================= ROLE =================

        Label roleLabel = new Label("Role");
        styleLabel(roleLabel);

        ComboBox<String> role = new ComboBox<>();

        role.getItems().addAll(

                "Student",

                "Admin",

                "Worker"

        );

        role.setValue("Student");

        role.setMaxWidth(Double.MAX_VALUE);

        // ================= DEPARTMENT =================

        Label departmentLabel =

                new Label("Department");

        ComboBox<String> department =

                new ComboBox<>();

        department.getItems().addAll(

                DEPARTMENTS

        );

        department.setPromptText(

                "Select Department"

        );

        department.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(department);

        department.setVisible(false);

        department.setManaged(false);

        departmentLabel.setVisible(false);

        departmentLabel.setManaged(false);

        // ================= ROLE ACTION =================

        role.setOnAction(e -> {

            boolean isWorker =

                    "Worker".equals(role.getValue());

            department.setVisible(isWorker);

            department.setManaged(isWorker);

            departmentLabel.setVisible(isWorker);

            departmentLabel.setManaged(isWorker);

        });

        // ================= REGISTER BUTTON =================

        Button register =

                new Button("Register");

        register.setMaxWidth(Double.MAX_VALUE);

        register.setOnAction(e -> {

            String enteredName =

                    name.getText().trim();

            String enteredUsername =

                    username.getText().trim();

            String enteredPassword =

                    password.getText();

            String enteredConfirmPassword =

                    confirmPassword.getText();

            String selectedRole =

                    role.getValue();

            // ================= BASIC VALIDATION =================

            if (enteredName.isEmpty() ||

                    enteredUsername.isEmpty() ||

                    enteredPassword.isEmpty() ||

                    enteredConfirmPassword.isEmpty()) {

                showAlert(

                        "Please fill all required fields."

                );

                return;

            }

            // ================= REGISTRATION NUMBER =================

            if (enteredUsername.length() != 10) {

                showAlert(

                        "Registration number must contain exactly 10 characters."

                );

                return;

            }

            // ================= PASSWORD MATCH =================

            if (!enteredPassword.equals(

                    enteredConfirmPassword)) {

                showAlert(

                        "Passwords do not match."

                );

                return;

            }

            // ================= WORKER DEPARTMENT =================

            String selectedDepartment = null;

            if (selectedRole.equals("Worker")) {

                if (department.getValue() == null) {

                    showAlert(

                            "Please select worker department."

                    );

                    return;

                }

                selectedDepartment =

                        department.getValue();

            }

            // ================= REGISTER =================

            boolean registered =

                    Database.registerUser(

                            enteredUsername,

                            enteredPassword,

                            enteredName,

                            selectedRole,

                            selectedDepartment

                    );

            if (registered) {

                showAlert(

                        "Registration successful!\n\n" +

                        "You can now login using your registration number."

                );

                showLogin(stage);

            } else {

                showAlert(

                        "Registration failed.\n\n" +

                        "Registration number may already exist."

                );

            }

        });

        // ================= FORM =================

        form.getChildren().addAll(

                nameLabel,

                name,

                usernameLabel,

                username,

                passwordLabel,

                password,

                confirmPasswordLabel,

                confirmPassword,

                roleLabel,

                role,

                departmentLabel,

                department,

                register

        );

        root.setCenter(form);

        stage.setScene(

                new Scene(root, 900, 600)

        );

    }

    // ================= COMMON LAYOUT =================

    private BorderPane baseLayout(

            String title,

            Runnable backAction

    ) {

        BorderPane root =

                new BorderPane();

        root.setStyle(

                getMainStyle()

        );

        // ================= TOP BAR =================

        HBox topBar =

                new HBox();

        topBar.setPadding(

                new Insets(18, 25, 18, 25)

        );

        topBar.setAlignment(

                Pos.CENTER_LEFT

        );

        Label titleLabel =

                new Label(title);

        titleLabel.setStyle(

                "-fx-text-fill: white;" +

                "-fx-font-size: 22px;" +

                "-fx-font-weight: bold;"

        );

        Region spacer =

                new Region();

        HBox.setHgrow(

                spacer,

                Priority.ALWAYS

        );

        Button back =

                new Button("← Back");

        back.setStyle(

                "-fx-background-color: rgba(255,255,255,0.20);" +

                "-fx-text-fill: white;" +

                "-fx-font-size: 13px;" +

                "-fx-font-weight: bold;" +

                "-fx-background-radius: 10;" +

                "-fx-padding: 8 15 8 15;" +

                "-fx-cursor: hand;"

        );

        back.setOnAction(

                e -> backAction.run()

        );

        topBar.getChildren().addAll(

                titleLabel,

                spacer,

                back

        );

        topBar.setStyle(

                "-fx-background-color: linear-gradient(to right, #312E81, #6C63FF, #8B5CF6);"

        );

        root.setTop(topBar);

        return root;

    }

    // ================= STUDENT DASHBOARD =================

 // ================= STUDENT DASHBOARD =================

    private void showStudentDashboard(

            String student,

            String username

    ) {

        BorderPane root =

                baseLayout(

                        "Student Dashboard",

                        () -> showLogin(stage)

                );

        // ================= LEFT MENU =================

        VBox menu =

                new VBox(18);

        menu.setPadding(

                new Insets(25)

        );

        menu.setPrefWidth(230);

        menu.setStyle(

                "-fx-background-color: white;" +

                "-fx-border-color: #E0E7FF;" +

                "-fx-border-width: 0 1 0 0;"

        );

        Label menuTitle =

                new Label("STUDENT MENU");

        menuTitle.setStyle(

                "-fx-text-fill: #6C63FF;" +

                "-fx-font-size: 13px;" +

                "-fx-font-weight: bold;"

        );

        Button submit =

                new Button("📝  Submit Complaint");

        Button myComplaints =

                new Button("📋  My Complaints");

        Button track =

                new Button("🔍  Track Complaint");

        submit.setMaxWidth(

                Double.MAX_VALUE

        );

        myComplaints.setMaxWidth(

                Double.MAX_VALUE

        );

        track.setMaxWidth(

                Double.MAX_VALUE

        );

        styleButton(submit);

        styleButton(myComplaints);

        styleButton(track);

        submit.setOnAction(

                e -> showComplaintForm(

                        student,

                        username

                )

        );

        myComplaints.setOnAction(

                e -> showMyComplaints(

                        student,

                        username

                )

        );

        track.setOnAction(

                e -> showMyComplaints(

                        student,

                        username

                )

        );

        menu.getChildren().addAll(

                menuTitle,

                submit,

                myComplaints,

                track

        );

        root.setLeft(menu);

        // ================= CENTER =================

        VBox center =

                new VBox(20);

        center.setAlignment(

                Pos.CENTER

        );

        Label welcome =

                new Label(

                        "Welcome, " + student + " 👋"

                );

        welcome.setStyle(

                "-fx-text-fill: #312E81;" +

                "-fx-font-size: 30px;" +

                "-fx-font-weight: bold;"

        );

        Label info =

                new Label(

                        "Manage your complaints easily and track their progress."

                );

        info.setStyle(

                "-fx-text-fill: #64748B;" +

                "-fx-font-size: 15px;"

        );

        VBox welcomeCard =

                new VBox(12);

        welcomeCard.setAlignment(

                Pos.CENTER

        );

        welcomeCard.setPadding(

                new Insets(35)

        );

        welcomeCard.setMaxWidth(550);

        welcomeCard.setStyle(

                "-fx-background-color: white;" +

                "-fx-background-radius: 20;" +

                "-fx-border-radius: 20;" +

                "-fx-border-color: #E0E7FF;" +

                "-fx-effect: dropshadow(gaussian, rgba(79,70,229,0.15), 20, 0.2, 0, 5);"

        );

        welcomeCard.getChildren().addAll(

                welcome,

                info

        );

        center.getChildren().add(

                welcomeCard

        );

        root.setCenter(center);

        stage.setScene(

                new Scene(root, 1000, 650)

        );

    }

    // ================= COMPLAINT FORM =================

    private void showComplaintForm(

            String student,

            String username

    ) {

        BorderPane root =

                baseLayout(

                        "Submit Complaint",

                        () -> showStudentDashboard(

                                student,

                                username

                        )

                );

        VBox form =

                new VBox(15);

        form.setPadding(

                new Insets(30)

        );

        ComboBox<String> category =

                new ComboBox<>();

        // SAME DEPARTMENTS EVERYWHERE

        category.getItems().addAll(

                DEPARTMENTS

        );

        category.setPromptText(

                "Select Department"

        );

        TextArea description =

                new TextArea();

        description.setPromptText(

                "Enter complaint description"

        );

        description.setPrefRowCount(6);

        Button submit =

                new Button("🚀  Submit Complaint");

        styleComboBox(category);

        styleTextArea(description);

        styleButton(submit);

        Label departmentLabel =

                new Label("Department");

        Label descriptionLabel =

                new Label("Description");

        styleLabel(departmentLabel);

        styleLabel(descriptionLabel);

        submit.setOnAction(e -> {

            if (category.getValue() == null ||

                    description.getText()

                            .trim()

                            .isEmpty()) {

                showAlert(

                        "Please fill all fields."

                );

                return;

            }

            String complaintId =

                    "CMP" +

                    System.currentTimeMillis();

            boolean saved =

                    Database.addComplaint(

                            complaintId,

                            username,

                            category.getValue(),

                            description.getText()

                                    .trim()

                    );

            if (saved) {

                showAlert(

                        "Complaint submitted successfully.\n\n" +

                        "Complaint ID: " +

                        complaintId

                );

                showStudentDashboard(

                        student,

                        username

                );

            } else {

                showAlert(

                        "Failed to submit complaint."

                );

            }

        });

        form.getChildren().addAll(

                departmentLabel,

                category,

                descriptionLabel,

                description,

                submit

        );

        root.setCenter(form);

        stage.setScene(

                new Scene(root, 900, 600)

        );

    }

    // ================= MY COMPLAINTS =================

    private void showMyComplaints(

            String student,

            String username

    ) {

        BorderPane root =

                baseLayout(

                        "My Complaints",

                        () -> showStudentDashboard(student, username)

                );

        VBox list =

                new VBox(15);

        list.setPadding(

                new Insets(20)

        );

        List<Complaint> complaints =

                Database.getAllComplaints();

        boolean found = false;

        for (Complaint c :

                complaints) {

            if (!c.getStudent()

                    .equals(student)) {

                continue;

            }

            found = true;

            VBox card =

                    new VBox(8);

            card.setPadding(

                    new Insets(15)

            );

            card.setStyle(

                    "-fx-border-color: #cccccc;" +

                    "-fx-border-radius: 8;" +

                    "-fx-background-radius: 8;"

            );

            Label id =

                    new Label(

                            "Complaint ID: " +

                            c.getComplaintId()

                    );

            Label department =

                    new Label(

                            "Department: " +

                            c.getDepartment()

                    );

            Label description =

                    new Label(

                            "Description: " +

                            c.getDescription()

                    );

            Label status =

                    new Label(

                            "Status: " +

                            c.getStatus()

                    );

            Label worker =

                    new Label(

                            "Worker: " +

                            c.getWorker()

                    );

            card.getChildren().addAll(

                    id,

                    department,

                    description,

                    status,

                    worker

            );

            if (c.getResolution() != null &&

                    !c.getResolution().isEmpty()) {

                Label resolution =

                        new Label(

                                "Resolution: " +

                                c.getResolution()

                        );

                card.getChildren().add(

                        resolution

                );

            }

            // ================= RESOLVED =================

            if (c.getStatus()

                    .equals("Resolved")) {

                Button accept =

                        new Button(

                                "Accept & Close"

                        );
        styleButton(accept);

                Button reopen =

                        new Button(

                                "Reopen Complaint"

                        );
        styleButton(reopen);

                accept.setOnAction(e -> {

                    Database.updateStatus(

                            c.getComplaintId(),

                            "Closed"

                    );

                    showMyComplaints(

                            student,

                            username

                    );

                });

                reopen.setOnAction(e -> {

                    Database.updateStatus(

                            c.getComplaintId(),

                            "Reopened"

                    );

                    showAlert(

                            "Complaint reopened successfully."

                    );

                    showMyComplaints(

                            student,

                            username

                    );

                });

                HBox buttons =

                        new HBox(

                                10,

                                accept,

                                reopen

                        );

                card.getChildren().add(

                        buttons

                );

            }

            // ================= STATUS COLOUR =================

            if (c.getStatus().equals("Resolved") ||

                    c.getStatus().equals("Closed")) {

                status.setTextFill(

                        Color.GREEN

                );

            } else {

                status.setTextFill(

                        Color.RED

                );

            }

            list.getChildren().add(card);

        }

        if (!found) {

            list.getChildren().add(

                    new Label(

                            "No complaints found."

                    )

            );

        }

        ScrollPane scrollPane =

                new ScrollPane(list);

        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        root.setCenter(scrollPane);

        stage.setScene(

                new Scene(root, 900, 600)

        );

    }

    private void searchStudentComplaints() {

        BorderPane root =

                baseLayout(

                        "Search Student Complaints",

                        () -> showAdminDashboard()

                );

        VBox mainBox = new VBox(15);

        mainBox.setPadding(new Insets(25));

        Label title =

                new Label("Search Complaints by Registration Number");

        title.setStyle(

                "-fx-font-size: 20px;" +

                "-fx-font-weight: bold;"

        );

        TextField registrationNumber =

                new TextField();

        registrationNumber.setPromptText(

                "Enter student registration number"

        );

        registrationNumber.setMaxWidth(350);

        Button search =

                new Button("Search");
        styleButton(search);

        HBox searchBox =

                new HBox(10);

        searchBox.getChildren().addAll(

                registrationNumber,

                search

        );

        VBox results =

                new VBox(12);

        ScrollPane scroll =

                new ScrollPane(results);

        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        search.setOnAction(e -> {

            String regNo =

                    registrationNumber.getText().trim();

            if (regNo.isEmpty()) {

                showAlert(

                        "Please enter a registration number."

                );

                return;

            }

            results.getChildren().clear();

            List<Complaint> complaints =

                    Database.getAllComplaints();

            boolean found = false;

            for (Complaint c : complaints) {

                if (regNo.equalsIgnoreCase(

                        c.getStudent()

                )) {

                    found = true;

                    VBox card =

                            new VBox(8);

                    card.setPadding(

                            new Insets(15)

                    );

                    card.setStyle(

                            "-fx-border-color: #cccccc;" +

                            "-fx-border-radius: 8;"

                    );

                    Label id =

                            new Label(

                                    "Complaint ID: " +

                                    c.getComplaintId()

                            );

                    Label student =

                            new Label(

                                    "Student: " +

                                    c.getStudent()

                            );

                    Label category =

                            new Label(

                                    "Department: " +

                                    c.getCategory()

                            );

                    Label description =

                            new Label(

                                    "Description: " +

                                    c.getDescription()

                            );

                    Label department =

                            new Label(

                                    "Assigned Department: " +

                                    c.getDepartment()

                            );

                    Label worker =

                            new Label(

                                    "Assigned Worker: " +

                                    c.getWorker()

                            );

                    Label status =

                            new Label(

                                    "Status: " +

                                    c.getStatus()

                            );

                    if ("Resolved".equals(

                            c.getStatus()) ||

                        "Closed".equals(

                            c.getStatus())) {

                        status.setTextFill(

                                Color.GREEN

                        );

                    } else {

                        status.setTextFill(

                                Color.RED

                        );

                    }

                    Label resolution =

                            new Label(

                                    "Resolution: " +

                                    (c.getResolution() == null ||

                                     c.getResolution().isEmpty()

                                            ? "Not available"

                                            : c.getResolution())

                            );

                    card.getChildren().addAll(

                            id,

                            student,

                            category,

                            description,

                            department,

                            worker,

                            status,

                            resolution

                    );

                    results.getChildren().add(card);

                }

            }

            if (!found) {

                Label noResult =

                        new Label(

                                "No complaints found for registration number: "

                                        + regNo

                        );

                noResult.setStyle(

                        "-fx-text-fill: red;" +

                        "-fx-font-size: 14px;"

                );

                results.getChildren().add(

                        noResult

                );

            }

        });

        mainBox.getChildren().addAll(

                title,

                searchBox,

                scroll

        );

        root.setCenter(mainBox);

        stage.setScene(

                new Scene(root, 900, 600)

        );

    }

    // ================= ADMIN DASHBOARD =================

    private void showAdminDashboard() {

        BorderPane root =

                baseLayout(

                        "Admin Dashboard",

                        () -> showLogin(stage)

                );

        VBox main =

                new VBox(15);

        main.setPadding(

                new Insets(20)

        );

        List<Complaint> complaints =

                Database.getAllComplaints();

        int total = complaints.size();

        int submitted = 0;

        int assigned = 0;

        int inProgress = 0;

        int reopened = 0;

        int resolved = 0;

        int closed = 0;

        for (Complaint c :

                complaints) {

            switch (c.getStatus()) {

                case "Submitted":

                    submitted++;

                    break;

                case "Assigned":

                    assigned++;

                    break;

                case "In Progress":

                    inProgress++;

                    break;

                case "Reopened":

                    reopened++;

                    break;

                case "Resolved":

                    resolved++;

                    break;

                case "Closed":

                    closed++;

                    break;

            }

        }

        // ================= STATISTICS =================

        Label totalLabel =

                new Label(

                        "Total Complaints: " +

                        total

                );

        Label submittedLabel =

                new Label(

                        "Submitted: " +

                        submitted

                );

        Label assignedLabel =

                new Label(

                        "Assigned: " +

                        assigned

                );

        Label progressLabel =

                new Label(

                        "In Progress: " +

                        inProgress

                );

        Label reopenedLabel =

                new Label(

                        "Reopened: " +

                        reopened

                );

        Label resolvedLabel =

                new Label(

                        "Resolved: " +

                        resolved

                );

        Label closedLabel =

                new Label(

                        "Closed: " +

                        closed

                );

        reopenedLabel.setTextFill(

                Color.RED

        );

        resolvedLabel.setTextFill(

                Color.GREEN

        );

        closedLabel.setTextFill(

                Color.GREEN

        );

        // ================= BUTTONS =================

        Button manage =

                new Button(

                        "Manage Complaints"

                );
        styleButton(manage);

        Button reopenedButton =

                new Button(

                        "Reopened Complaints (" +

                        reopened +

                        ")"

                );
        styleButton(reopenedButton);

        Button searchStudent =

                new Button("Search Student Complaints");
        styleButton(searchStudent);

        searchStudent.setOnAction(e ->

                searchStudentComplaints()

        );

        manage.setOnAction(

                e -> showAdminComplaints()

        );

        reopenedButton.setOnAction(

                e -> showReopenedComplaints()

        );

        HBox buttons =

                new HBox(

                        10,

                        manage,

                        reopenedButton,

                        searchStudent

                );

        // ================= ALL COMPLAINTS LIST =================

        Label listTitle =

                new Label(

                        "All Complaints"

                );

        listTitle.setFont(

                Font.font(20)

        );

        VBox complaintList =

                new VBox(10);

        for (Complaint c :

                complaints) {

            VBox card =

                    createAdminComplaintCard(c);

            complaintList.getChildren().add(card);

        }

        if (complaints.isEmpty()) {

            complaintList.getChildren().add(

                    new Label(

                            "No complaints available."

                    )

            );

        }

        ScrollPane scroll =

                new ScrollPane(

                        complaintList

                );

        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        scroll.setPrefHeight(350);

        main.getChildren().addAll(

                totalLabel,

                submittedLabel,

                assignedLabel,

                progressLabel,

                reopenedLabel,

                resolvedLabel,

                closedLabel,

                buttons,

                listTitle,

                scroll

        );

        root.setCenter(main);

        stage.setScene(

                new Scene(root, 900, 600)

        );

    }

    // ================= ADMIN COMPLAINT CARD =================

    private VBox createAdminComplaintCard(

            Complaint c

    ) {

        VBox card =

                new VBox(7);

        card.setPadding(

                new Insets(15)

        );

        card.setStyle(

                "-fx-border-color: #cccccc;" +

                "-fx-border-radius: 8;" +

                "-fx-background-radius: 8;"

        );

        Label id =

                new Label(

                        "Complaint ID: " +

                        c.getComplaintId()

                );

        Label student =

                new Label(

                        "Student Reg No: " +

                        c.getStudent()

                );

        Label department =

                new Label(

                        "Department: " +

                        c.getDepartment()

                );

        Label worker =

                new Label(

                        "Assigned Worker: " +

                        c.getWorker()

                );

        Label description =

                new Label(

                        "Description: " +

                        c.getDescription()

                );

        Label status =

                new Label(

                        "Status: " +

                        c.getStatus()

                );

        status.setFont(

                Font.font(15)

        );

        // GREEN = SOLVED

        if (c.getStatus().equals("Resolved") ||

                c.getStatus().equals("Closed")) {

            status.setTextFill(

                    Color.GREEN

            );

        // RED = NOT SOLVED / REOPENED

        } else {

            status.setTextFill(

                    Color.RED

            );

        }

        card.getChildren().addAll(

                id,

                student,

                department,

                worker,

                description,

                status

        );

        if (c.getResolution() != null &&

                !c.getResolution().isEmpty()) {

            Label resolution =

                    new Label(

                            "Resolution: " +

                            c.getResolution()

                    );

            card.getChildren().add(

                    resolution

            );

        }

        return card;

    }

    // ================= REOPENED COMPLAINTS =================

    private void showReopenedComplaints() {

        BorderPane root =

                baseLayout(

                        "Reopened Complaints",

                        () -> showAdminDashboard()

                );

        VBox list =

                new VBox(15);

        list.setPadding(

                new Insets(20)

        );

        List<Complaint> complaints =

                Database.getAllComplaints();

        boolean found = false;

        for (Complaint c :

                complaints) {

            if (!c.getStatus()

                    .equals("Reopened")) {

                continue;

            }

            found = true;

            VBox card =

                    createAdminComplaintCard(c);

            list.getChildren().add(card);

        }

        if (!found) {

            list.getChildren().add(

                    new Label(

                            "No reopened complaints."

                    )

            );

        }

        ScrollPane scroll =

                new ScrollPane(list);

        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        root.setCenter(scroll);

        stage.setScene(

                new Scene(root, 900, 600)

        );

    }

    // ================= ADMIN COMPLAINT MANAGEMENT =================

    private void showAdminComplaints() {

        BorderPane root =

                baseLayout(

                        "Manage Complaints",

                        () -> showAdminDashboard()

                );

        VBox list =

                new VBox(15);

        list.setPadding(

                new Insets(20)

        );

        List<Complaint> complaints =

                Database.getAllComplaints();

        complaints.removeIf(

                c -> "Resolved".equals(c.getStatus()) ||

                     "Closed".equals(c.getStatus())

        );

        for (Complaint c :

                complaints) {

            VBox card =

                    new VBox(8);

            card.setPadding(

                    new Insets(15)

            );

            card.setStyle(

                    "-fx-border-color: #cccccc;" +

                    "-fx-border-radius: 8;"

            );

            Label id =

                    new Label(

                            "Complaint ID: " +

                            c.getComplaintId()

                    );

            Label student =

                    new Label(

                            "Student Reg No: " +

                            c.getStudent()

                    );

            Label category =

                    new Label(

                            "Department: " +

                            c.getCategory()

                    );

            Label description =

                    new Label(

                            "Description: " +

                            c.getDescription()

                    );

            Label status =

                    new Label(

                            "Status: " +

                            c.getStatus()

                    );

            if (c.getStatus().equals("Resolved") ||

                    c.getStatus().equals("Closed")) {

                status.setTextFill(

                        Color.GREEN

                );

            } else {

                status.setTextFill(

                        Color.RED

                );

            }

            ComboBox<String> department =

                    new ComboBox<>();

            department.getItems().addAll(

                    DEPARTMENTS

            );

            if (c.getDepartment() != null &&

                    !c.getDepartment()

                            .equals("Not Assigned")) {

                department.setValue(

                        c.getDepartment()

                );

            }

            ComboBox<String> worker =

                    new ComboBox<>();

            if (department.getValue() != null) {

                worker.getItems().addAll(

                        Database.getWorkersByDepartment(

                                department.getValue()

                        )

                );

            }

            if (c.getWorker() != null &&

                    !c.getWorker()

                            .equals("Not Assigned") &&

                    !worker.getItems()

                            .contains(c.getWorker())) {

                worker.getItems().add(

                        c.getWorker()

                );

            }

            if (c.getWorker() != null &&

                    !c.getWorker()

                            .equals("Not Assigned")) {

                worker.setValue(

                        c.getWorker()

                );

            }

            department.setOnAction(e -> {

                worker.getItems().clear();

                String selectedDepartment =

                        department.getValue();

                if (selectedDepartment != null) {

                    worker.getItems().addAll(

                            Database.getWorkersByDepartment(

                                    selectedDepartment

                            )

                    );

                }

                worker.setValue(null);

            });

            Button assign =

                    new Button(

                            "Assign Worker"

                    );
        styleButton(assign);

            assign.setOnAction(e -> {

                if (department.getValue() == null ||

                        worker.getValue() == null) {

                    showAlert(

                            "Select department and worker."

                    );

                    return;

                }

                boolean assigned =

                        Database.assignComplaint(

                                c.getComplaintId(),

                                department.getValue(),

                                worker.getValue()

                        );

                if (assigned) {

                    showAlert(

                            "Complaint Assigned Successfully!\n\n" +

                            "Complaint ID: " +

                            c.getComplaintId() +

                            "\nDepartment: " +

                            department.getValue() +

                            "\nWorker: " +

                            worker.getValue()

                    );

                }

                showAdminComplaints();

            });

            TextField resolution =

                    new TextField();

            resolution.setPromptText(

                    "Resolution note"

            );

            if (c.getResolution() != null) {

                resolution.setText(

                        c.getResolution()

                );

            }

            Button saveResolution =

                    new Button(

                            "Save Resolution"

                    );
        styleButton(saveResolution);

            saveResolution.setOnAction(e -> {

                if (resolution.getText()

                        .trim()

                        .isEmpty()) {

                    showAlert(

                            "Enter resolution."

                    );

                    return;

                }

                Database.updateResolution(

                        c.getComplaintId(),

                        resolution.getText()

                                .trim()

                );

                showAdminComplaints();

            });

            card.getChildren().addAll(

                    id,

                    student,

                    category,

                    description,

                    status,

                    new Label("Department"),

                    department,

                    new Label("Worker"),

                    worker,

                    assign,

                    resolution,

                    saveResolution

            );

            list.getChildren().add(card);

        }

        ScrollPane scroll =

                new ScrollPane(list);

        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        root.setCenter(scroll);

        stage.setScene(

                new Scene(root, 900, 600)

        );

    }

    // ================= WORKER DASHBOARD =================

    private void showWorkerDashboard(

            String workerName

    ) {

        BorderPane root =

                baseLayout(

                        "Worker Dashboard",

                        () -> showLogin(stage)

                );

        VBox list =

                new VBox(15);

        list.setPadding(

                new Insets(20)

        );

        List<Complaint> complaints =

                Database.getAllComplaints();

        boolean found = false;

        for (Complaint c :

                complaints) {

            if (c.getWorker() == null ||

                    !c.getWorker()

                            .equals(workerName)) {

                continue;

            }

            found = true;

            VBox card =

                    new VBox(8);

            card.setPadding(

                    new Insets(15)

            );

            card.setStyle(

                    "-fx-border-color: #cccccc;" +

                    "-fx-border-radius: 8;"

            );

            Label id =

                    new Label(

                            "Complaint ID: " +

                            c.getComplaintId()

                    );

            Label department =

                    new Label(

                            "Department: " +

                            c.getDepartment()

                    );

            Label category =

                    new Label(

                            "Category: " +

                            c.getCategory()

                    );

            Label description =

                    new Label(

                            "Description: " +

                            c.getDescription()

                    );

            Label status =

                    new Label(

                            "Status: " +

                            c.getStatus()

                    );

            if (c.getStatus().equals("Resolved") ||

                    c.getStatus().equals("Closed")) {

                status.setTextFill(

                        Color.GREEN

                );

            } else {

                status.setTextFill(

                        Color.RED

                );

            }

            card.getChildren().addAll(

                    id,

                    department,

                    category,

                    description,

                    status

            );

            if (c.getStatus()

                    .equals("Assigned")) {

                Button start =

                        new Button(

                                "Start Work"

                        );
        styleButton(start);

                start.setOnAction(event -> {

                    Database.updateStatus(

                            c.getComplaintId(),

                            "In Progress"

                    );

                    showWorkerDashboard(

                            workerName

                    );

                });

                card.getChildren().add(

                        start

                );

            }

            if (c.getStatus()

                    .equals("In Progress") ||

                    c.getStatus()

                            .equals("Reopened")) {

                TextField resolution =

                        new TextField();

                resolution.setPromptText(

                        "Enter resolution"

                );

                Button resolve =

                        new Button(

                                "Mark Resolved"

                        );
        styleButton(resolve);

                resolve.setOnAction(event -> {

                    if (resolution.getText()

                            .trim()

                            .isEmpty()) {

                        showAlert(

                                "Enter resolution."

                        );

                        return;

                    }

                    Database.updateResolution(

                            c.getComplaintId(),

                            resolution.getText()

                                    .trim()

                    );

                    showWorkerDashboard(

                            workerName

                    );

                });

                card.getChildren().addAll(

                        resolution,

                        resolve

                );

            }

            list.getChildren().add(card);

        }

        if (!found) {

            list.getChildren().add(

                    new Label(

                            "No complaints assigned to you."

                    )

            );

        }

        ScrollPane scroll =

                new ScrollPane(list);

        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        root.setCenter(scroll);

        stage.setScene(

                new Scene(root, 900, 600)

        );

    }

    // ================= ALERT =================

    private void showAlert(

            String message

    ) {

        Alert alert =

                new Alert(

                        Alert.AlertType.INFORMATION

                );

        alert.setTitle(

                "Smart Complaint System"

        );

        alert.setHeaderText(null);

        alert.setContentText(

                message

        );

        alert.showAndWait();

    }

    public static void main(

            String[] args

    ) {

        launch(args);

    }

 // ================= STYLING =================

    private String getMainStyle() {

        return

                "-fx-background-color: linear-gradient(to bottom right, #E8F1FF, #F8F5FF);";

    }

    // ================= BUTTON STYLE =================

    private void styleButton(Button button) {

        button.setStyle(

                "-fx-background-color: linear-gradient(to right, #6C63FF, #8B5CF6);" +

                "-fx-text-fill: white;" +

                "-fx-font-size: 14px;" +

                "-fx-font-weight: bold;" +

                "-fx-background-radius: 12;" +

                "-fx-border-radius: 12;" +

                "-fx-padding: 10 18 10 18;" +

                "-fx-cursor: hand;"

        );

        button.setOnMouseEntered(e ->

                button.setStyle(

                        "-fx-background-color: linear-gradient(to right, #584FF0, #7C3AED);" +

                        "-fx-text-fill: white;" +

                        "-fx-font-size: 14px;" +

                        "-fx-font-weight: bold;" +

                        "-fx-background-radius: 12;" +

                        "-fx-border-radius: 12;" +

                        "-fx-padding: 10 18 10 18;" +

                        "-fx-cursor: hand;" +

                        "-fx-effect: dropshadow(gaussian, rgba(108,99,255,0.35), 10, 0.3, 0, 3);"

                )

        );

        button.setOnMouseExited(e ->

                styleButton(button)

        );

    }

    // ================= TITLE STYLE =================

    private void styleTitle(Label label) {

        label.setStyle(

                "-fx-text-fill: #312E81;" +

                "-fx-font-size: 26px;" +

                "-fx-font-weight: bold;"

        );

    }

    // ================= NORMAL LABEL =================

    private void styleLabel(Label label) {

        label.setStyle(

                "-fx-text-fill: #374151;" +

                "-fx-font-size: 14px;"

        );

    }

    // ================= TEXT FIELD =================

    private void styleTextField(TextField field) {

        field.setStyle(

                "-fx-background-color: white;" +

                "-fx-border-color: #C7D2FE;" +

                "-fx-border-width: 1.5;" +

                "-fx-border-radius: 10;" +

                "-fx-background-radius: 10;" +

                "-fx-padding: 10;" +

                "-fx-font-size: 14px;"

        );

    }

    // ================= PASSWORD FIELD =================

    private void stylePasswordField(PasswordField field) {

        field.setStyle(

                "-fx-background-color: white;" +

                "-fx-border-color: #C7D2FE;" +

                "-fx-border-width: 1.5;" +

                "-fx-border-radius: 10;" +

                "-fx-background-radius: 10;" +

                "-fx-padding: 10;" +

                "-fx-font-size: 14px;"

        );

    }

    // ================= TEXT AREA =================

    private void styleTextArea(TextArea area) {

        area.setStyle(

                "-fx-background-color: white;" +

                "-fx-border-color: #C7D2FE;" +

                "-fx-border-width: 1.5;" +

                "-fx-border-radius: 10;" +

                "-fx-background-radius: 10;" +

                "-fx-font-size: 14px;" +

                "-fx-padding: 8;"

        );

    }

    // ================= COMBO BOX =================

    private void styleComboBox(ComboBox<String> comboBox) {

        comboBox.setStyle(

                "-fx-background-color: white;" +

                "-fx-border-color: #C7D2FE;" +

                "-fx-border-radius: 10;" +

                "-fx-background-radius: 10;" +

                "-fx-font-size: 14px;"

        );

    }

    // ================= CARD STYLE =================

    private void styleCard(VBox card) {

        card.setStyle(

                "-fx-background-color: white;" +

                "-fx-background-radius: 16;" +

                "-fx-border-radius: 16;" +

                "-fx-border-color: #E0E7FF;" +

                "-fx-border-width: 1;" +

                "-fx-padding: 15;" +

                "-fx-effect: dropshadow(gaussian, rgba(79,70,229,0.12), 12, 0.2, 0, 4);"

        );

    }

}
