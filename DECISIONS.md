***DECISIONS***

# Rule Representation
- **Decision**: I chose to represent rules as Java classes implementing a common interface (`ValidationRule`). Each rule encapsulates its own validation logic and error message.
- I considered using a database to define rules thinking it would allow dynamic updates without code changes, but ultimately decided against it due to future challenges data normalization as rules become more complex and models change.

# Rule Priority
- **Decision**: I decided that all rules should be applied to each application without a specific priority order as there is no current situation where an application is valid even when validation fails. If future rules required a WARN priority I would implement rule priorities.

# Error Handling
- **Decision**: I decided that all rules should be applied to each application and collect all errors rather than stopping at the first failure. Allowing the user to see all validation issues at once provides a better user experience and reduces the number of iterations needed to fix an application.
- I initially considered validating min/max values and pattern validation at the controller level to short-circuit invalid requests. I ultimately decided against it to provide multiple errors at once.
- I recognize my current implementation still short circuits for my enum and date values making it inconsistent with the rest of the behavior. Due to time constraints and Jackson configuration issues, I decided to leave it as is but would refactor in the future to be consistent.

# Rule Conflicts
- **Decision**: I have added the ability to enable and disable rules via a flag. This allows for flexibility in handling rule conflicts as I can disable conflicting rules until they are resolved. 
- As conflicting rules are added, a priority logic for rules can be implemented
- For current overlap such as SSN, I have implemented a Util class to handle validation across multiple rules.

# Performance
- **Decision**: I decided to implement a simple in-memory rule engine that applies all rules sequentially. This is sufficient for the current scope of the application and allows for easy addition of new rules.
- If the number of rules or applications increases significantly, I would consider optimizing the rule application process by implementing parallel processing or conditional rule execution based on dependencies between rules.

# Extensibility
- **Decision**: I designed the system with extensibility in mind by using a common interface for validation rules. This allows for easy addition of rules as new requirements arise and the application object evolves.

# Severity Levels
- Similar to rule priority, I have not categorized different types of violations with severity levels as there is no current situation where an application is valid even when validation fails. If future rules required a WARN severity level I would implement severity levels.
- I recognize my current implementation should include log.warn statements for failed rules with a WARN severity level and would add this in the future.

# Testing Strategy
- All service and rule methods are fully covered
- Tests are set up to test features independently of each other. For example, the SSN validation tests only test the SSN validation logic and not the entire application validation process.

# Time Spent
- I spend roughly 2.5 hours in dev time on this project