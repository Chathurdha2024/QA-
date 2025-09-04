// tests/api/login.test.js
const chai = require("chai");
const chaiHttp = require("chai-http");
const expect = chai.expect;

chai.use(chaiHttp);

const BASE_URL = "http://localhost:8081"; // Spring Boot backend must be running

describe("Login API", () => {
  it("should fail with invalid credentials", (done) => {
    chai.request(BASE_URL)
      .post("/api/login")
      .send({
        username: "wronguser",
        password: "wrongpass"
      })
      .end((err, res) => {
        expect(res).to.have.status(400);
        expect(res.body).to.have.property("message", "Invalid username or password");
        done();
      });
  });

  it("should login successfully with valid credentials", (done) => {
    // ⚠️ this assumes the user exists (created via signup)
    const testUser = {
      username: "apitestuser_login",
      email: "apitestuser_login@example.com",
      password: "password123"
    };

    // First register the user
    chai.request(BASE_URL)
      .post("/api/addUser")
      .send(testUser)
      .end(() => {
        // Then try login
        chai.request(BASE_URL)
          .post("/api/login")
          .send({
            username: testUser.username,
            password: testUser.password
          })
          .end((err, res) => {
            expect(res).to.have.status(200);
            expect(res.body).to.have.property("message", "Login successful");
            expect(res.body).to.have.property("token");
            done();
          });
      });
  });
});
