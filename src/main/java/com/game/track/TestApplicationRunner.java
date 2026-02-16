package com.game.track;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestApplicationRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Game Track API Application Started Successfully!");
        System.out.println("Endpoints available:");
        System.out.println("- POST /api/auth/register - Register new user");
        System.out.println("- POST /api/auth/login - Login user");
        System.out.println("- GET /api/projects - Get projects");
        System.out.println("- POST /api/projects - Create project");
        System.out.println("- GET /api/tasks - Get tasks");
        System.out.println("- POST /api/tasks - Create task");
        System.out.println("- GET /api/users - Get users");
        System.out.println("Application is ready to use!");
    }
}