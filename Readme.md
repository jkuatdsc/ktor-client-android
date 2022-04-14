## Ktor In Android

# This repo is for the ktor in android session

Before trying out the OAuth2 part you need a client secret and Id that you can get from the google
cloud console add them to the properties.local file as below. You can take a look at the app level
gradle file to see how to add the properties.local file properties are extracted.

```
client.secret=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
client.id=354856893310-p64r9g9u8selht6771pfislt6po3529f.apps.googleusercontent.com
```

Be sure to give them some time before using them in the app. They normally take about 5 minutes to
be active once you create them. Make sure to also add the gmail scope and also configure you consent
screen in the console.

<br>

---

For more information on the OAuth2 part see
the [Google documentation](https://developers.google.com/identity/protocols/OAuth2).

---

For more information on the gmail api see
the [Google documentation](https://developers.google.com/gmail/api/v1/reference/users/messages).

---

For more information on the ktor in android see the [Ktor docs](https://ktor.io).
