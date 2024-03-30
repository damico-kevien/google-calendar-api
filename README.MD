# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Create a Google Cloud project](https://developers.google.com/workspace/guides/create-project?hl=en)
* [Enable Google Workspace APIs](https://developers.google.com/workspace/guides/enable-apis?hl=en)
* [Learn about authentication and authorization](https://developers.google.com/workspace/guides/auth-overview?hl=en)
* [Configure the OAuth consent screen and choose scopes](https://developers.google.com/workspace/guides/configure-oauth-consent?hl=en)
* [Create access credentials](https://developers.google.com/workspace/guides/create-credentials?hl=en)

---

### How to use it

First at all, download your credential secrets. Use `client_secret.json.example`: paste your properties inside the JSON
without change the structure and rename the file in `client_secret.json`.
Go to `http://localhost:8080/api/v1/oauth2/login` via browser, enable your Google Account to use your application and
copy the access token. Paste it inside `endpoints/http-client.env.json` and enjoy!

N.B.: Remember to apply the right environment in `endpoints/google.http` before try to call the endpoint. (See the
screenshot below)

![env.png](src%2Fmain%2Fresources%2Fenv.png)