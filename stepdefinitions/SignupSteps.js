const { When, Then } = require('@cucumber/cucumber');
const assert = require('assert');

let userRepo = {};
let response = {};

When('I signup with name {string}, email {string} and password {string}', function (username, email, password) {
  if (!username || !password || !email.includes('@')) {
    response = { status: 400, body: { message: "Invalid signup input" } };
    return;
  }

  if (userRepo[username]) {
    response = { status: 400, body: { message: "Username already exists" } };
  } else {
    userRepo[username] = { username, email, password };
    response = { status: 200, body: { message: "User registered successfully" } };
  }
});

Then('the signup should be successful', function () {
  assert.strictEqual(response.status, 200);
  assert.strictEqual(response.body.message, "User registered successfully");
});

Then('I should see a signup failure', function () {
  assert.strictEqual(response.status, 400);
});
