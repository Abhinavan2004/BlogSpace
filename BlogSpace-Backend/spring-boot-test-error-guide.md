# Spring Boot Test Configuration Error - Complete Guide

## 📋 Table of Contents
1. [Error Overview](#error-overview)
2. [Understanding the Error](#understanding-the-error)
3. [Root Cause Analysis](#root-cause-analysis)
4. [Why This Happens](#why-this-happens)
5. [Solutions (Step by Step)](#solutions-step-by-step)
6. [Best Practices](#best-practices)
7. [Troubleshooting Checklist](#troubleshooting-checklist)
8. [Additional Resources](#additional-resources)

---

## 🚨 Error Overview

### The Error Message
```
Connection to localhost:5432 refused. Check that the hostname and port are correct 
and that the postmaster is accepting TCP/IP connections.
```

### What It Means (In Simple Terms)
Your test is trying to connect to PostgreSQL database (port 5432) instead of the H2 in-memory database you configured for testing. It's like calling the wrong phone number - the test is looking for a database that isn't running.

### Error Type
- **Category**: Configuration Error
- **Severity**: Critical (Tests cannot run)
- **Scope**: Test Environment Only

---

## 🔍 Understanding the Error

### What Should Happen (Expected Behavior)

```
┌─────────────────────────────────────────┐
│         Test Execution Flow             │
├─────────────────────────────────────────┤
│                                         │
│  1. Test starts                        │
│  2. Load application-test.properties   │
│  3. Connect to H2 (in-memory DB)       │
│  4. Run tests                          │
│  5. Tests pass ✓                       │
│                                         │
└─────────────────────────────────────────┘
```

### What Actually Happened (Actual Behavior)

```
┌─────────────────────────────────────────┐
│         Test Execution Flow             │
├─────────────────────────────────────────┤
│                                         │
│  1. Test starts                        │
│  2. Load application.properties (MAIN) │  ❌ Wrong file!
│  3. Try to connect to PostgreSQL      │  ❌ DB not running!
│  4. Connection refused                 │
│  5. Tests fail ✗                       │
│                                         │
└─────────────────────────────────────────┘
```

---

## 🎯 Root Cause Analysis

### The Core Problem

Spring Boot is loading the **wrong configuration file** during testing.

```
Your Project Structure:
├── src/
│   ├── main/
│   │   └── resources/
│   │       └── application.properties  ← Uses PostgreSQL (localhost:5432)
│   │
│   └── test/
│       └── resources/
│           └── application-test.properties  ← Uses H2 (in-memory)
```

**Problem**: Even though you have `application-test.properties`, Spring Boot is reading `application.properties` instead.

### Technical Deep Dive

#### Step 1: Spring Boot Property Loading Order

Spring Boot loads properties in a specific order (highest priority first):

```
Priority Level (1 = Highest)
════════════════════════════════════════════════════════════════

1. @TestPropertySource (inline properties)      ← HIGHEST
2. @TestPropertySource (file locations)
3. @SpringBootTest (properties attribute)
4. application-{profile}.properties in test/resources
5. application.properties in test/resources
6. application-{profile}.properties in main/resources
7. application.properties in main/resources     ← LOWEST
```

#### Step 2: Profile Activation Chain

```java
@SpringBootTest
@ActiveProfiles("test")  ← This should activate "test" profile
class BlogSpaceBackendApplicationTests {
    // ...
}
```

When `@ActiveProfiles("test")` is used, Spring should:
1. Load `application.properties` (base config)
2. Load `application-test.properties` (override with test config)
3. Merge them (test properties override main properties)

#### Step 3: What Went Wrong

The profile-specific properties file wasn't being loaded because:

**Possible Reasons:**
1. ✗ Build tool didn't copy test resources to classpath
2. ✗ IDE cache was stale
3. ✗ File wasn't in correct location
4. ✗ Profile name mismatch (e.g., "test" vs "tests")

---

## 🤔 Why This Happens

### Reason 1: Maven/Gradle Build Configuration

**Maven Example:**

If your `pom.xml` is missing test resources configuration:

```xml
<!-- ❌ MISSING OR INCORRECT -->
<build>
    <testResources>
        <testResource>
            <directory>src/test/resources</directory>
        </testResource>
    </testResources>
</build>
```

**Result**: Test properties file doesn't get copied to `target/test-classes/`

### Reason 2: IDE Caching Issues

IntelliJ IDEA and other IDEs cache compiled files. If you:
1. Created `application-test.properties` recently
2. Didn't rebuild the project

**Result**: IDE runs tests with old cached configuration

### Reason 3: Profile Name Mismatch

```
File name:       application-test.properties    ← Singular "test"
Annotation:      @ActiveProfiles("tests")       ← Plural "tests"
                                  
Result: MISMATCH! File won't be loaded.
```

### Reason 4: Classpath Issues

The test properties file must be on the classpath:

```
Expected location:
target/test-classes/application-test.properties

If missing from classpath → File won't be found
```

---

## ✅ Solutions (Step by Step)

### Solution 1: Using Inline Properties (Quick Fix)

**Difficulty**: ⭐ Beginner  
**When to use**: Quick testing, debugging  
**Pros**: Always works, explicit  
**Cons**: Properties hardcoded in Java file

```java
package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BlogSpaceBackendApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("✓ Test context loaded successfully!");
    }
}
```

**Explanation:**
- `@TestPropertySource(properties = {...})` has the highest priority
- These properties override EVERYTHING else
- No external file needed

**When this solution works:**
- ✓ Always (guaranteed to work)
- ✓ Good for troubleshooting
- ✗ Not maintainable for large test suites

---

### Solution 2: Explicit File Location (Recommended)

**Difficulty**: ⭐⭐ Beginner-Intermediate  
**When to use**: Production test suites  
**Pros**: Properties in separate file, reusable  
**Cons**: Requires correct file structure

#### Step 2.1: Create/Verify Test Properties File

**Location**: `src/test/resources/application-test.properties`

```properties
# ========================================
# H2 In-Memory Database Configuration
# ========================================
# This creates a temporary database in RAM
# It's destroyed after tests complete
spring.datasource.url=jdbc:h2:mem:testdb

# H2 Driver
spring.datasource.driver-class-name=org.h2.Driver

# Default H2 credentials
spring.datasource.username=sa
spring.datasource.password=

# ========================================
# JPA/Hibernate Configuration
# ========================================
# Tell Hibernate to use H2 dialect
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Recreate schema for each test
spring.jpa.hibernate.ddl-auto=create-drop

# Show SQL queries (helpful for debugging)
spring.jpa.show-sql=true

# Format SQL output
spring.jpa.properties.hibernate.format_sql=true
```

#### Step 2.2: Update Test Class

```java
package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BlogSpaceBackendApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("✓ Test context loaded successfully!");
    }
}
```

**Explanation:**
- `locations = "classpath:application-test.properties"` explicitly tells Spring where to find the file
- `classpath:` means "look in src/test/resources/"
- This overrides the default profile mechanism

**Why this is better than Solution 1:**
- ✓ Properties in separate file (easier to maintain)
- ✓ Can be shared across multiple test classes
- ✓ Still explicit (not relying on auto-detection)

---

### Solution 3: Standard Profile Approach (Advanced)

**Difficulty**: ⭐⭐⭐ Intermediate  
**When to use**: Large projects with multiple profiles  
**Pros**: Spring Boot standard, automatic  
**Cons**: Requires proper setup

This is the "correct" way, but requires everything to be configured properly.

#### Step 3.1: Verify File Structure

```
your-project/
├── pom.xml  (or build.gradle)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       └── BlogSpaceBackendApplication.java
│   │   └── resources/
│   │       └── application.properties          ← Main config
│   │
│   └── test/
│       ├── java/
│       │   └── com/example/demo/
│       │       └── BlogSpaceBackendApplicationTests.java
│       └── resources/
│           └── application-test.properties     ← Test config
```

#### Step 3.2: Verify Maven Configuration (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <!-- ... other configuration ... -->
    
    <build>
        <plugins>
            <!-- Maven Compiler Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
            </plugin>
            
            <!-- Maven Surefire Plugin (runs tests) -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0-M9</version>
            </plugin>
        </plugins>
        
        <!-- IMPORTANT: Test Resources Configuration -->
        <testResources>
            <testResource>
                <directory>src/test/resources</directory>
                <filtering>true</filtering>
            </testResource>
        </testResources>
    </build>
</project>
```

#### Step 3.3: Clean and Rebuild

**Using Maven:**
```bash
# Delete all compiled files
mvn clean

# Compile and run tests
mvn test

# Or compile without running tests
mvn clean install -DskipTests
```

**Using IntelliJ IDEA:**
1. Go to: **File → Invalidate Caches**
2. Check all boxes
3. Click **Invalidate and Restart**

#### Step 3.4: Update Test Class

```java
package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // Activates "test" profile
class BlogSpaceBackendApplicationTests {

    // Inject the datasource URL to verify correct config
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Test
    void contextLoads() {
        System.out.println("✓ Test context loaded successfully!");
        System.out.println("Using datasource: " + datasourceUrl);
        
        // Verify we're using H2
        assert datasourceUrl.contains("h2:mem") : 
            "Expected H2 database but got: " + datasourceUrl;
    }
}
```

**Explanation:**
- `@ActiveProfiles("test")` tells Spring to load `application-test.properties`
- `@Value` annotation injects the datasource URL
- We verify we're using H2 (not PostgreSQL)

---

### Solution 4: Using YAML Instead of Properties

**Difficulty**: ⭐⭐ Beginner-Intermediate  
**When to use**: If `.properties` files have issues  
**Pros**: Better structure, same functionality  
**Cons**: Different syntax

#### Step 4.1: Create Test YAML File

**Location**: `src/test/resources/application-test.yml`

```yaml
# ========================================
# Test Configuration (YAML Format)
# ========================================

spring:
  # Datasource Configuration
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: ""
  
  # JPA Configuration
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true

# Logging (optional)
logging:
  level:
    org.springframework: INFO
    com.example.demo: DEBUG
```

#### Step 4.2: Use Standard Profile Approach

```java
@SpringBootTest
@ActiveProfiles("test")
class BlogSpaceBackendApplicationTests {
    @Test
    void contextLoads() {
        // Test implementation
    }
}
```

**Note**: YAML and properties files work identically in Spring Boot.

---

## 📊 Comparison of Solutions

| Solution | Maintainability | Ease of Use | Recommended For | Works Always? |
|----------|----------------|-------------|-----------------|---------------|
| **Inline Properties** | ⭐ | ⭐⭐⭐⭐⭐ | Quick debugging | ✓ Yes |
| **Explicit File** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Most projects | ✓ Yes |
| **Standard Profile** | ⭐⭐⭐⭐⭐ | ⭐⭐ | Large projects | ⚠️ Needs setup |
| **YAML Format** | ⭐⭐⭐⭐ | ⭐⭐⭐ | Personal preference | ⚠️ Needs setup |

### Recommended Approach

**For Beginners**: Use **Solution 2** (Explicit File Location)
- Easy to understand
- Always works
- Good balance of simplicity and maintainability

**For Production**: Use **Solution 3** (Standard Profile Approach)
- Industry standard
- Scales well
- But requires proper setup

---

## 🎓 Best Practices

### 1. Test Database Strategy

```
Development Environment:
├── application.properties          → PostgreSQL (real database)
│
Test Environment:
├── application-test.properties     → H2 (in-memory database)
│
Production Environment:
├── application-prod.properties     → PostgreSQL (production database)
```

**Why use H2 for tests?**
- ✓ Fast (runs in memory)
- ✓ No external dependencies
- ✓ Clean state for each test
- ✓ No need to install PostgreSQL

### 2. Profile Naming Convention

```
✓ GOOD:
- application-test.properties
- application-dev.properties
- application-prod.properties

✗ BAD:
- application-tests.properties    (plural)
- application_test.properties     (underscore)
- applicationtest.properties      (no separator)
```

### 3. Test Properties Organization

```properties
# ========================================
# SECTION 1: Database Configuration
# ========================================
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver

# ========================================
# SECTION 2: JPA Configuration
# ========================================
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# ========================================
# SECTION 3: Logging Configuration
# ========================================
logging.level.org.springframework=INFO
```

### 4. Multiple Test Classes Sharing Configuration

**Base Test Configuration Class:**

```java
package com.example.demo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class BaseIntegrationTest {
    // Common test setup can go here
}
```

**Individual Test Classes:**

```java
package com.example.demo;

import org.junit.jupiter.api.Test;

class UserServiceTest extends BaseIntegrationTest {
    @Test
    void testUserCreation() {
        // Test implementation
    }
}

class BlogPostServiceTest extends BaseIntegrationTest {
    @Test
    void testBlogPostCreation() {
        // Test implementation
    }
}
```

**Benefits:**
- Configuration defined once
- Reused across all test classes
- Easy to maintain

---

## 🔧 Troubleshooting Checklist

Use this checklist to debug configuration issues:

### Pre-Flight Checks

```
□ H2 dependency is in pom.xml (or build.gradle)
□ Test properties file exists in src/test/resources/
□ File naming is correct (application-test.properties)
□ Profile annotation matches file name (@ActiveProfiles("test"))
□ No typos in property names
```

### Build Tool Checks

```
Maven:
□ Run: mvn clean test -X (verbose output)
□ Check: target/test-classes/application-test.properties exists
□ Verify: <testResources> configured in pom.xml

Gradle:
□ Run: ./gradlew clean test --info
□ Check: build/resources/test/application-test.properties exists
□ Verify: test resources configured in build.gradle
```

### IDE Checks

```
IntelliJ IDEA:
□ Invalidate caches: File → Invalidate Caches
□ Reimport Maven/Gradle project
□ Check: Mark directory as test resources (right-click folder)
□ Run tests from IDE (not command line)

Eclipse:
□ Clean project: Project → Clean
□ Refresh: F5 on project
□ Check: src/test/resources is in build path
```

### Runtime Verification

Add this to your test to see which properties are loaded:

```java
@SpringBootTest
@ActiveProfiles("test")
class BlogSpaceBackendApplicationTests {

    @Autowired
    private Environment environment;

    @Test
    void verifyTestProfile() {
        // Check active profiles
        String[] profiles = environment.getActiveProfiles();
        System.out.println("Active profiles: " + Arrays.toString(profiles));
        
        // Check datasource URL
        String datasourceUrl = environment.getProperty("spring.datasource.url");
        System.out.println("Datasource URL: " + datasourceUrl);
        
        // Verify it's H2
        assertTrue(datasourceUrl.contains("h2"), 
            "Expected H2 database but got: " + datasourceUrl);
    }
}
```

---

## 🐛 Common Errors and Solutions

### Error 1: "Unable to determine Dialect"

```
Caused by: org.hibernate.HibernateException: 
Unable to determine Dialect without JDBC metadata
```

**Cause**: Hibernate doesn't know which SQL dialect to use

**Solution**: Add dialect property
```properties
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

---

### Error 2: "Connection refused localhost:5432"

```
Connection to localhost:5432 refused
```

**Cause**: Test is using PostgreSQL config instead of H2

**Solution**: 
1. Verify test properties file is being loaded
2. Use explicit `@TestPropertySource` annotation
3. Check profile name matches file name

---

### Error 3: "ClassNotFoundException: org.h2.Driver"

```
java.lang.ClassNotFoundException: org.h2.Driver
```

**Cause**: H2 dependency not in classpath

**Solution**: Add H2 to pom.xml
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

---

### Error 4: "Failed to load ApplicationContext"

```
java.lang.IllegalStateException: 
Failed to load ApplicationContext
```

**Cause**: Multiple possible issues (database, configuration, beans)

**Solution**: 
1. Check logs for root cause
2. Verify database configuration
3. Ensure all required beans are available
4. Check for circular dependencies

---

## 📚 Understanding Spring Boot Concepts

### What is a Profile?

Think of profiles as different "modes" your application can run in:

```
┌─────────────────────────────────────────────┐
│              Your Application               │
├─────────────────────────────────────────────┤
│                                             │
│  Profile: "dev"     → Use local database   │
│  Profile: "test"    → Use H2 in-memory     │
│  Profile: "prod"    → Use production DB    │
│                                             │
└─────────────────────────────────────────────┘
```

### What is @SpringBootTest?

```java
@SpringBootTest
class MyTest {
    // This loads the ENTIRE Spring application context
    // Including all beans, configurations, etc.
}
```

**What it does:**
1. Starts a mini version of your application
2. Loads all Spring beans
3. Configures database connections
4. Sets up JPA/Hibernate
5. Makes everything available for testing

**Alternatives:**
- `@WebMvcTest` - Only loads web layer
- `@DataJpaTest` - Only loads JPA components
- `@MockBean` - Creates mock beans

### What is Classpath?

```
Classpath = List of places Java looks for files

During Tests:
├── target/test-classes/     ← Test files here
├── target/classes/          ← Main files here
├── ~/.m2/repository/        ← Maven dependencies
└── JDK libraries            ← Java standard library
```

When you use `classpath:application-test.properties`:
1. Java searches all locations in classpath
2. Finds `target/test-classes/application-test.properties`
3. Loads the file

---

## 🔬 Deep Dive: How Spring Loads Properties

### The Complete Loading Sequence

```
Step 1: Application starts
   ↓
Step 2: Spring Boot looks for property sources in this order:
   │
   ├─→ 1. @TestPropertySource (inline)        [HIGHEST PRIORITY]
   ├─→ 2. @TestPropertySource (locations)
   ├─→ 3. @SpringBootTest(properties)
   ├─→ 4. System properties (-Dkey=value)
   ├─→ 5. OS environment variables
   ├─→ 6. Profile-specific in test resources
   ├─→ 7. application.properties in test resources
   ├─→ 8. Profile-specific in main resources
   └─→ 9. application.properties in main resources [LOWEST PRIORITY]
   ↓
Step 3: Merge all properties (higher priority overrides lower)
   ↓
Step 4: Configure Spring beans with merged properties
   ↓
Step 5: Application ready for testing
```

### Example Merge Process

**File 1**: `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/BlogSpace
spring.datasource.username=Abhinav
spring.datasource.password=Abhi@2004
spring.jpa.show-sql=true
server.port=8080
```

**File 2**: `src/test/resources/application-test.properties`
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
```

**Merged Result** (what Spring actually uses):
```properties
spring.datasource.url=jdbc:h2:mem:testdb          ← From test file
spring.datasource.username=sa                      ← From test file
spring.datasource.password=                        ← From test file
spring.jpa.show-sql=true                          ← From main file (not overridden)
server.port=8080                                  ← From main file (not overridden)
```

---

## 💡 Real-World Example

Let's create a complete working example:

### Project Structure

```
BlogSpace-Backend/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── BlogSpaceBackendApplication.java
│   │   │   ├── model/
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java
│   │   │   └── service/
│   │   │       └── UserService.java
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       ├── java/com/example/demo/
│       │   ├── BlogSpaceBackendApplicationTests.java
│       │   └── service/
│       │       └── UserServiceTest.java
│       └── resources/
│           └── application-test.properties
```

### 1. Main Application Properties

**File**: `src/main/resources/application.properties`

```properties
# ========================================
# Production Database Configuration
# ========================================
spring.application.name=BlogSpace-Backend

spring.datasource.url=jdbc:postgresql://localhost:5432/BlogSpace?options=-c%20TimeZone=UTC
spring.datasource.username=Abhinav
spring.datasource.password=Abhi@2004

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server Configuration
server.port=8080
```

### 2. Test Properties

**File**: `src/test/resources/application-test.properties`

```properties
# ========================================
# Test Database Configuration (H2)
# ========================================
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration for Tests
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Disable specific features for tests
spring.jpa.open-in-view=false

# Logging
logging.level.org.springframework.test=DEBUG
logging.level.com.example.demo=DEBUG
```

### 3. Entity Class

**File**: `src/main/java/com/example/demo/model/User.java`

```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String email;
    
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
```

### 4. Repository Interface

**File**: `src/main/java/com/example/demo/repository/UserRepository.java`

```java
package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
}
```

### 5. Service Class

**File**: `src/main/java/com/example/demo/service/UserService.java`

```java
package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User createUser(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        User user = new User(username, email);
        return userRepository.save(user);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
```

### 6. Context Test

**File**: `src/test/java/com/example/demo/BlogSpaceBackendApplicationTests.java`

```java
package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BlogSpaceBackendApplicationTests {

    @Autowired
    private Environment environment;
    
    @Autowired
    private DataSource dataSource;
    
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Test
    void contextLoads() {
        // Verify context loaded successfully
        assertNotNull(environment, "Environment should not be null");
        System.out.println("✓ Application context loaded successfully!");
    }
    
    @Test
    void verifyTestProfile() {
        // Check active profiles
        String[] profiles = environment.getActiveProfiles();
        System.out.println("Active profiles: " + Arrays.toString(profiles));
        
        // Check datasource URL
        System.out.println("Datasource URL: " + datasourceUrl);
        
        // Verify we're using H2
        assertTrue(datasourceUrl.contains("h2:mem"), 
            "Expected H2 in-memory database but got: " + datasourceUrl);
        
        System.out.println("✓ Test is using H2 in-memory database");
    }
    
    @Test
    void verifyDatabaseConnection() throws Exception {
        // Test actual database connection
        assertNotNull(dataSource, "DataSource should not be null");
        
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");
            
            String databaseName = connection.getMetaData().getDatabaseProductName();
            System.out.println("Connected to: " + databaseName);
            
            assertEquals("H2", databaseName, "Should be connected to H2 database");
            
            System.out.println("✓ Successfully connected to H2 database");
        }
    }
}
```

### 7. Service Test

**File**: `src/test/java/com/example/demo/service/UserServiceTest.java`

```java
package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional  // Rollback after each test
class UserServiceTest {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    void setUp() {
        // Clean database before each test
        userRepository.deleteAll();
        System.out.println("Database cleaned for test");
    }
    
    @Test
    void testCreateUser() {
        // Given
        String username = "testuser";
        String email = "test@example.com";
        
        // When
        User createdUser = userService.createUser(username, email);
        
        // Then
        assertNotNull(createdUser.getId(), "User ID should be generated");
        assertEquals(username, createdUser.getUsername());
        assertEquals(email, createdUser.getEmail());
        
        System.out.println("✓ User created successfully: " + createdUser);
    }
    
    @Test
    void testCreateDuplicateUser() {
        // Given
        String username = "testuser";
        userService.createUser(username, "test1@example.com");
        
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(username, "test2@example.com");
        });
        
        assertEquals("Username already exists", exception.getMessage());
        System.out.println("✓ Duplicate username correctly rejected");
    }
    
    @Test
    void testGetAllUsers() {
        // Given
        userService.createUser("user1", "user1@example.com");
        userService.createUser("user2", "user2@example.com");
        userService.createUser("user3", "user3@example.com");
        
        // When
        List<User> users = userService.getAllUsers();
        
        // Then
        assertEquals(3, users.size(), "Should have 3 users");
        System.out.println("✓ Retrieved all users: " + users.size());
    }
    
    @Test
    void testFindByUsername() {
        // Given
        String username = "testuser";
        String email = "test@example.com";
        userService.createUser(username, email);
        
        // When
        User foundUser = userService.findByUsername(username);
        
        // Then
        assertNotNull(foundUser);
        assertEquals(username, foundUser.getUsername());
        assertEquals(email, foundUser.getEmail());
        
        System.out.println("✓ User found by username: " + foundUser);
    }
    
    @Test
    void testFindNonExistentUser() {
        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.findByUsername("nonexistent");
        });
        
        assertEquals("User not found", exception.getMessage());
        System.out.println("✓ Non-existent user correctly throws exception");
    }
}
```

### 8. Running the Tests

**From Command Line:**

```bash
# Clean and run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=BlogSpaceBackendApplicationTests

# Run with verbose output
mvn test -X
```

**Expected Output:**

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.demo.BlogSpaceBackendApplicationTests
✓ Application context loaded successfully!
Active profiles: []
Datasource URL: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
✓ Test is using H2 in-memory database
Connected to: H2
✓ Successfully connected to H2 database
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0

[INFO] Running com.example.demo.service.UserServiceTest
Database cleaned for test
✓ User created successfully: User(id=1, username=testuser, email=test@example.com)
Database cleaned for test
✓ Duplicate username correctly rejected
Database cleaned for test
✓ Retrieved all users: 3
Database cleaned for test
✓ User found by username: User(id=1, username=testuser, email=test@example.com)
Database cleaned for test
✓ Non-existent user correctly throws exception
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0

[INFO] Results:
[INFO] 
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## 🎯 Key Takeaways

### For Beginners

1. **Tests should use a separate database** (H2) from development (PostgreSQL)
2. **Properties files control which database to use**
3. **Profile names must match** file names exactly
4. **When in doubt**, use explicit `@TestPropertySource` annotation
5. **Always verify** which database your test is using

### For Intermediate Developers

1. Understand Spring's property loading order
2. Use profiles consistently across environments
3. Keep test configuration separate and isolated
4. Use `@Transactional` to rollback test data
5. Leverage Spring Boot's test slices (@DataJpaTest, @WebMvcTest)

### For Advanced Developers

1. Create base test classes for configuration reuse
2. Use TestContainers for integration tests with real databases
3. Implement custom test property sources
4. Use @TestConfiguration for test-specific beans
5. Profile-specific testing strategies for different scenarios

---

## 📖 Additional Resources

### Official Documentation

- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Spring Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
- [H2 Database](http://www.h2database.com/html/main.html)

### Recommended Reading

1. **Spring Boot in Action** by Craig Walls
2. **Testing Spring Boot Applications** (Spring.io Guides)
3. **JUnit 5 User Guide**

### Video Tutorials

- Spring Boot Testing Tutorial (YouTube)
- JUnit 5 Crash Course
- Spring Profiles Explained

### Community Resources

- [Stack Overflow - Spring Boot Tag](https://stackoverflow.com/questions/tagged/spring-boot)
- [Spring Boot GitHub Issues](https://github.com/spring-projects/spring-boot/issues)
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-boot)

---

## 🆘 Getting Help

### Before Asking for Help

1. ✓ Check this guide thoroughly
2. ✓ Run the diagnostic commands
3. ✓ Review error logs completely
4. ✓ Try the suggested solutions
5. ✓ Search for similar issues online

### When Asking for Help

Include:
1. Full error stacktrace
2. Relevant configuration files (pom.xml, application.properties)
3. Test class code
4. Spring Boot version
5. What you've already tried

### Where to Ask

- [Stack Overflow](https://stackoverflow.com/questions/tagged/spring-boot) - Best for technical questions
- [Spring Community Forums](https://community.spring.io/) - Official support
- [Reddit r/SpringBoot](https://reddit.com/r/SpringBoot) - Community discussions
- [GitHub Issues](https://github.com/spring-projects/spring-boot/issues) - Bug reports

---

## ✅ Final Checklist

Before you consider this issue resolved:

```
□ Tests run successfully (mvn test passes)
□ Tests use H2 database (verified with logging)
□ No connection errors to PostgreSQL
□ All test cases pass
□ Properties file is properly loaded
□ Configuration is maintainable
□ Code is properly documented
□ Team members understand the setup
```

---

## 📝 Summary

This error occurs when Spring Boot tests try to connect to PostgreSQL instead of H2. The root cause is that the test configuration file isn't being loaded properly.

**Quick Fix**: Use `@TestPropertySource(locations = "classpath:application-test.properties")`

**Proper Fix**: Ensure your build tool copies test resources to classpath and Spring profiles are configured correctly.

**Remember**: Tests should be fast, isolated, and repeatable. Using H2 in-memory database achieves all three goals.

---

**Document Version**: 1.0  
**Last Updated**: 2026-02-13  
**Author**: Created for Spring Boot Learning  
**License**: Free to use and modify

---

## 📞 Need More Help?

If this guide didn't solve your issue:

1. Review the troubleshooting section again
2. Check if you're using a different Spring Boot version
3. Verify all dependencies are correctly configured
4. Consider posting on Stack Overflow with specific details

Good luck with your testing! 🚀
