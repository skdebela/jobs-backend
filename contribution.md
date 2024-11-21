## Git Branching Structure
- **main branch** (`main`): Production-ready code.
- **development branch** (`dev`): Staging area for completed features.
- **feature branches** (`<name>/<feature>`): For individual features, bug fixes, or components.
    - Examples:
        - `samuel/auth`
        - `amanuel/jobs-api`

---

## Workflow

#### **1. Pull the Latest Changes from `dev`**
- Before starting any work, ensure your local environment is in sync:
  ```bash
  git checkout dev
  git pull origin dev
  ```

#### **2. Create a New Feature Branch**
- Create a branch for your specific task:
  ```bash
  git checkout -b <your-name>/<feature>
  ```
  Example:
  ```bash
  git checkout -b samuel/auth
  ```

#### **3. Work on Your Feature Locally**
- Make changes and commit frequently:
  ```bash
  git add .
  git commit -m "Add user authentication"
  ```

#### **4. Sync Your Feature Branch with `dev`**
- To avoid conflicts later, keep your branch up-to-date:
  ```bash
  git checkout dev
  git pull origin dev
  git checkout <your-name>/<feature>
  git merge dev
  ```
    - Fix any conflicts that arise and re-test.

#### **5. Push Your Feature Branch and Create a Pull Request**
- Once your feature is complete:
  ```bash
  git push origin <your-name>/<feature>
  ```
- Create a pull request (PR) on GitHub to merge your feature branch into `dev`.
- Since it's just the two of us, **assign your teammate as a reviewer**.
    - If we're both satisfied with the changes, approve and merge the PR.
    - Discuss any feedback over a quick chat if needed.

#### **6. Merge the Pull Request**
- After approval, **merge the PR** into the `dev` branch via GitHub.
- Delete the branch (locally and on the remote) if no longer needed:
  ```bash
  git branch -d <your-name>/<feature>
  git push origin --delete <your-name>/<feature>
  ```

#### **7. Merging `dev` into `main`**
- Once `dev` is stable and has the new features:
    1. Create a PR to merge `dev` into `main`.
    2. Perform a final review together, ensuring all tests pass.
    3. Merge the PR and deploy to production.

#### **8. Deploying `main` for Frontend Team**
- When ready for production:
    - Deploy `main` to the staging environment (Render) so the frontend team can access the updated API.

---

## Best Practices
- Keep your **feature branches short-lived**—aim to complete and merge quickly to avoid conflicts.
