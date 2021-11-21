# Command Documentation

### Key Identifiers

| Identifier   | Required?        | Color  |
|--------------|------------------|--------|
| < >          | Yes  :thumbsup:  | <span class="color:red">Red</span>
| ( )          | No :thumbsdown:  | <span class="color:green">Green</span>

- Some commands will require certain token inputs, and without them they will not function. They will be noted by the key identifiers above. There will also have color coding with them.
---
- Other identifiers to keep in mind:
    - @TextChannel = A mentioned text channel input.
    - @Role = A mentioned role input.
    - Type_ID = A mentioned type's id. 
        - (e.x. Category_ID = 1237129837981)
    - String = A string of text. 
    
## Quick Links
| [Setup Category](#setup-category)        | Reports        | Tickets        | Info          | [Utility Category](#utility-category)      |
|---------------|----------------|----------------|---------------|--------------|
| Setup Command | Report Command | Ticket Command | About Command | Help Command |
|               |                |                |               | Ping Command |
|               |                |                |               | Test Command |

<hr>

###Setup Category

<b>SetupCommand</b></br>
<b>Description:</b> This command allows you to configure certain channels, roles and categories to help Tickster properly function within your server. Some are required for me to even begin creating tickets or reports.
- `/setup <SubCommand> <SubCommand Option>`
- `/setup ticket_channel <@TextChannel>`
- `/setup log_channel <@TextChannel>`
- `/setup ticket_category <Category_ID>`
- `/setup report_channel <@TextChannel>`
- `/setup ticket_manager <@Role>`



