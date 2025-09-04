// tests/unit/signupUnit.test.js
import { expect } from "chai";
import { validateEmail } from "../../utils/validation.js";

describe("Signup Unit Tests", function() {

  it('should pass for valid email', function() {
    expect(validateEmail('hasangi@gmail.com')).to.be.true;
  });

  it('should fail for invalid email (missing @)', function() {
    expect(validateEmail('hasangi.com')).to.be.false;
  });

  it('should fail for invalid email (missing domain)', function() {
    expect(validateEmail('hasangi@')).to.be.false;
  });

});
