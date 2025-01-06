## 📦🐋 Docker Image Registry
Here’s how the tagging system works:

- 📝 `draft-X` tags: These are builds for your Merge Requests (MRs). The image is built and tagged `draft-X` each time you push to a branch that has an open MR. So there is one image for each MR that is overriden at each push. It can be used to test deployment of an MR.
- 🔧 `dev-SHA` tags: These are builds created on every push, regardless if the branch is an MR. Each tag is based on the commit SHA. It is used by the CI pipeline to share the codebase between jobs.
- 🚢 `prod-SHA` tags: These are builds created for every push to the main branch, tagged with the commit SHA. So understand that after a fresh merge a new version of the image is built. It is used for deployment in production.

Find all images in our registry here: 👉 [Docker Hub Repository](https://hub.docker.com/repositories/silica5518)