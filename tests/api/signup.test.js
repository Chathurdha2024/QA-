// tests/api/signup.test.js
const chai = require("chai");
const chaiHttp = require("chai-http");
const expect = chai.expect;

chai.use(chaiHttp);

const BASE_URL = "http://localhost:8081"; // Spring Boot backend must be running

describe("Signup API", () => {
  it("should register a new user successfully", (done) => {
    chai.request(BASE_URL)
      .post("/api/addUser")
      .send({
        username: "apitestuser_" + Date.now(), // unique username each run
        email: "apitestuser_" + Date.now() + "@example.com",
        password: "password123"
      })
      .end((err, res) => {
        expect(res).to.have.status(201);
        expect(res.body).to.have.property("message", "User Registered Successfully");
        expect(res.body).to.have.property("id");
        expect(res.body).to.have.property("username");
        expect(res.body).to.have.property("email");
        done();
      });
  });

  it("should fail when required fields are missing", (done) => {
    chai.request(BASE_URL)
      .post("/api/addUser")
      .send({
        username: "onlyName"
        // missing email & password
      })
      .end((err, res) => {
        expect(res).to.have.status(400);
        expect(res.body).to.have.property("message", "Username, email and password are required");
        done();
      });
  });
});
