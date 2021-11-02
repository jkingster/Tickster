# Upcoming Feature(s)
* [ ] Ticket System
    - This system will allow guilds to have specific channels to configure their ticket system. 
      <br/> Maybe: `````/settings ticket [channel]`````
    - Tickets will be used to ultimately submit suggestions, reports, or any other needed information.
    - It will utilize slash command/button functionality. 
    - There will either be a "ticket managers" role required to see all tickets per guild. Or maybe we will implement a moderator role to see them. Administrators will always have access.
    - Maximum of 3 <b>OPEN</b> tickets per-user, to prevent ticket spamming. 
    - The idea is tickets will be closed then put into an archive up to 2 weeks, after those 2 weeks they will be deleted from the database internals to prevent clutter of data.
    - Need to create the table: ```SQL/guild_tickets.sql```, along with implementing it in jOOQ.
    - Subcommand(s): 
      - ```/ticket delete [ticket_id]```
      - ```/ticket edit [ticket_id]```
      - ```/ticket close [ticket_id]```
      - ```/ticket archives```
      - ```/ticket```
        - This will just provide a menu for the top three options if no arguments are provided.
      - The ticket_id is going to be just an integer 1-3 since you'll only be able to have three tickets at once.
    
    
* [ ] Moderation Command(s)
    - KickCommand (kick / kickbyid?)
    - BanCommand (ban / banbyid?)
    - Mute (Need to add to guild_data table)
    - Warnings? (Need to implement guild_warnings table.)
    
* [ ] Fun/Misc Ideas/Commands
    - 8Ball
    - Reddit API?
    - Imgur API?
    
* [ ] Cooldowns
    - This will prevent stupid spam of commands. Not every command will need a cooldown,
    basically only commands that have to make database queries.
      - Scopes: USER, GUILD, GLOBAL
    