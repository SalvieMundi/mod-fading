# Fading Help Page

## Config

For default values see: [settings](https://github.com/magistermaks/mod-fading/blob/master/src/main/java/net/darktree/fading/Settings.java)

**Burnout Times**  
_Integer values_  
Values `*.min` indicate minimum time in minutes  
Values `*.max` indicates maximum time in minutes  
`time.campfire.min`*  
`time.campfire.max`*  
`time.torch.min`  
`time.torch.max`  
`time.lantern.min`  
`time.lantern.max`  

**Item Durability**  
_Integer values_  
`item.durability.flint`  
`item.durability.gold`  
`item.durability.diamond`   

**Rain**  
_Integer values_  
Bigger values make the events more rare  
`rain.campfire`*  
`rain.torch`  
`rain.lantern`  

**Other**  
_Boolean value_  
If set to `true` makes torches disappear on burnout  
`other.disintegrate`  
If set to `false` custom flint and steels won't be add    
`other.flints`

_*Campfire time is per-layer (campfires has 3 layers)_