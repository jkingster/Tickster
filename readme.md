# Todo-List

## Part 1

- [X] Handle the insertion of new guilds.
- [ ] Restructure the handling of interactions (based on given component ids.)
- [ ] Create a reply system for all interactions (ReplyCallbackAction or just regular send messages?)
    - This includes Failure/Success responses.
- [X] Create a cache system that handles tickets, guild settings, and blacklisted users.
    - This will utilize ExpiringMap.
-  ~~Try and get the new permissions pr working, not sure if even implemented anywhere for JDA.~~
- [X] Implement ClassGraph to automatically register all interaction classes? Would simplify things.