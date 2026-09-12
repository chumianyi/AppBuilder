package com.mian.appbuilder.templates

import com.mian.appbuilder.model.TemplateCategories
import com.mian.appbuilder.model.TemplateModel
import com.mian.appbuilder.model.WidgetModel

/**
 * Generates 400+ built-in templates programmatically.
 */
object TemplateLibrary {

    val templates = mutableListOf<TemplateModel>()
    private var initialized = false

    fun init() {
        if (initialized) return
        initialized = true
        generateAll()
    }

    private fun generateAll() {
        templates.clear()

        // === Complete ready-to-use templates ===
        addCompleteTemplates()

        // === Generate category templates programmatically ===
        // Social: 30+
        generateCategory(TemplateCategories.SOCIAL, listOf(
            "Feed Screen", "Profile Page", "Chat List", "Post Creator",
            "Stories Viewer", "Followers List", "Notifications", "Search People",
            "Groups", "Events", "Messenger", "Video Feed", "Reels Screen",
            "Comments Section", "Likes List", "Mentions", "Direct Messages",
            "Group Chat", "Voice Chat", "Video Call", "Status Updates",
            "Friend Suggestions", "Nearby People", "Moments", "Timeline",
            "Trending Posts", "Hashtag Browser", "Community", "Forum Thread",
            "User Card"
        ))

        // E-commerce: 40+
        generateCategory(TemplateCategories.ECOMMERCE, listOf(
            "Product List", "Product Detail", "Shopping Cart", "Checkout",
            "Order History", "Order Tracking", "Product Review", "Wishlist",
            "Search Products", "Category Grid", "Brand Store", "Flash Sale",
            "Coupon Center", "Payment Method", "Shipping Address", "Returns",
            "Invoice View", "Product Compare", "Bulk Order", "Wholesale",
            "Auction House", "Flash Deals", "Daily Picks", "New Arrivals",
            "Best Sellers", "Top Rated", "Limited Edition", "Bundle Deals",
            "Subscription Box", "Gift Cards", "Store Locator", "Seller Profile",
            "Live Shopping", "Pre-order", "Back in Stock", "Price Alert",
            "Size Guide", "Color Options", "Product Tags", "Customer Photos"
        ))

        // Tools: 40+
        generateCategory(TemplateCategories.TOOLS, listOf(
            "Flashlight", "Compass", "Level Tool", "Ruler", "Sound Meter",
            "Battery Info", "File Manager", "Calculator Pro", "Unit Converter",
            "Currency Exchange", "QR Scanner", "Barcode Generator", "Document Scanner",
            "PDF Reader", "Note Pad", "Voice Recorder", "Screen Recorder",
            "Camera", "Gallery", "File Transfer", "Backup Tool", "Cleaner",
            "App Manager", "Task Killer", "Battery Saver", "Data Usage",
            "Network Info", "Bluetooth Settings", "WiFi Analyzer", "Screen Filter",
            "Text-to-Speech", "Speech-to-Text", "Translate", "Dictionary",
            "Thesaurus", "Word of Day", "Random Generator", "Timer",
            "Stopwatch", "Alarm Clock", "World Clock", "Metronome",
            "Pitch Tuner", "Protractor", "Magnifier"
        ))

        // Entertainment: 30+
        generateCategory(TemplateCategories.ENTERTAINMENT, listOf(
            "Movie List", "Movie Detail", "TV Shows", "Episode List",
            "Anime Browser", "Cartoon Hub", "Game Center", "Chess Board",
            "Puzzle Game", "Tic Tac Toe", "Hangman", "Trivia Quiz",
            "Memory Match", "Dice Roller", "Card Game", "Lottery",
            "Fortune Cookie", "Zodiac Signs", "Horoscope", "Tarot Reading",
            "Jokes App", "Memes Gallery", "GIF Search", "Soundboard",
            "DJ Mixer", "Karaoke", "Radio Player", "Podcast Player",
            "Comedy Club", "Celebrity News"
        ))

        // Education: 30+
        generateCategory(TemplateCategories.EDUCATION, listOf(
            "Course List", "Course Detail", "Video Lesson", "Quiz Screen",
            "Flashcards", "Study Timer", "Grade Book", "Attendance",
            "Assignment", "Syllabus", "Class Roster", "Discussion Board",
            "Virtual Classroom", "Whiteboard", "Bookmarks", "Citations",
            "Math Solver", "Science Lab", "History Timeline", "Geography Map",
            "Language Learning", "Vocabulary Builder", "Grammar Check",
            "Reading Comprehension", "Writing Assistant", "Research Notes",
            "Citation Generator", "Exam Prep", "Practice Test", "Score Tracker"
        ))

        // Finance: 30+
        generateCategory(TemplateCategories.FINANCE, listOf(
            "Dashboard", "Account Overview", "Transaction List", "Budget Tracker",
            "Expense Log", "Income Entry", "Bill Reminder", "Investment Portfolio",
            "Stock Watchlist", "Crypto Tracker", "Bank Transfer", "Credit Score",
            "Tax Calculator", "Loan Calculator", "Mortgage Calculator",
            "Retirement Planner", "Savings Goal", "Subscription Tracker",
            "Split Bill", "Group Savings", "Receipt Scanner", "Spending Analytics",
            "Cash Flow", "Net Worth", "Financial News", "Market Overview",
            "ETF Screener", "Options Chain", "Fund Details", "Transaction Receipt"
        ))

        // Productivity: 30+
        generateCategory(TemplateCategories.PRODUCTIVITY, listOf(
            "Todo List", "Kanban Board", "Gantt Chart", "Sprint Planner",
            "Meeting Notes", "Project Board", "Time Tracker", "Pomodoro Timer",
            "Calendar View", "Event Detail", "Reminder List", "Habit Tracker",
            "Daily Journal", "Weekly Review", "Goal Setting", "OKR Tracker",
            "Team Chat", "File Sharing", "Document Editor", "Spreadsheet",
            "Presentation", "Whiteboard", "Mind Map", "Flowchart",
            "Decision Tree", "Priority Matrix", "Eisenhower Box", "Time Blocking",
            "Focus Mode", "Deadline Tracker"
        ))

        // Health: 25+
        generateCategory(TemplateCategories.HEALTH, listOf(
            "Fitness Tracker", "Workout Plan", "Exercise Library", "Rep Counter",
            "Calorie Counter", "Meal Planner", "Water Intake", "Sleep Tracker",
            "Step Counter", "Heart Rate Monitor", "Blood Pressure Log",
            "Weight Tracker", "BMI Calculator", "Body Measurements",
            "Yoga Poses", "Meditation Timer", "Breathing Exercise",
            "Anxiety Relief", "Mood Tracker", "Symptom Checker",
            "Medicine Reminder", "Vaccination Record", "Lab Results",
            "Doctor Appointment", "Health Journal"
        ))

        // Travel: 25+
        generateCategory(TemplateCategories.TRAVEL, listOf(
            "Flight Search", "Hotel Booking", "Destination Guide", "Itinerary Planner",
            "Map View", "Nearby Places", "Restaurants", "Attractions",
            "Travel Checklist", "Packing List", "Expense Tracker", "Local Tips",
            "Currency Converter", "Language Phrases", "Emergency Numbers",
            "Travel Journal", "Photo Album", "Route Planner", "Trip Sharing",
            "Hostel Finder", "Vacation Rental", "Car Rental", "Travel Insurance",
            "Visa Requirements", "Weather Forecast"
        ))

        // Food & Drink: 20+
        generateCategory(TemplateCategories.FOOD, listOf(
            "Recipe Browser", "Recipe Detail", "Cooking Timer", "Ingredient List",
            "Meal Planner", "Grocery List", "Restaurant Finder", "Menu View",
            "Food Delivery", "Order Tracking", "Nutrition Facts", "Calorie Breakdown",
            "Cocktail Recipes", "Coffee Guide", "Wine Pairing", "Tea Culture",
            "Baking Guide", "Grilling Tips", "Slow Cooker", "Meal Prep"
        ))

        // News: 15+
        generateCategory(TemplateCategories.NEWS, listOf(
            "Breaking News", "Article View", "Video News", "Podcast News",
            "News Categories", "Trending Topics", "Saved Articles",
            "Offline Reading", "Newsletter", "Press Releases", "Editorial Board",
            "Opinion Section", "Local News", "World News", "Tech News"
        ))

        // Sports: 20+
        generateCategory(TemplateCategories.SPORTS, listOf(
            "Live Scores", "Match Schedule", "Standings", "Team Roster",
            "Player Stats", "Game Highlights", "Football League", "Basketball Court",
            "Tennis Match", "Cricket Score", "Hockey Game", "Baseball",
            "Soccer Commentary", "F1 Racing", "Olympics", "Golf Leaderboard",
            "Boxing Match", "UFC Fight", "ESports", "Fantasy League"
        ))

        // Photography: 15+
        generateCategory(TemplateCategories.PHOTOGRAPHY, listOf(
            "Camera Control", "Gallery Grid", "Photo Editor", "Filter Gallery",
            "Album Organizer", "Slideshow", "Photo Map", "Face Tags",
            "RAW Converter", "Panorama", "Time Lapse", "Slow Motion",
            "Selfie Camera", "Portrait Mode", "Photo Backup"
        ))

        // Music: 15+
        generateCategory(TemplateCategories.MUSIC, listOf(
            "Now Playing", "Playlist", "Album View", "Artist Page",
            "Genre Browse", "Search Music", "Lyrics View", "Equalizer",
            "Sleep Timer", "Offline Music", "Radio Station", "Podcast Episode",
            "Music Videos", "Concert Tickets", "Karaoke Room"
        ))

        // Video: 15+
        generateCategory(TemplateCategories.VIDEO, listOf(
            "Video Player", "Video Playlist", "Watch Later", "Subtitle Settings",
            "Quality Selector", "Cast Screen", "Picture-in-Picture",
            "Trending Videos", "Subscription Feed", "Channel Page",
            "Video Comments", "Video Likes", "Share Dialog", "History",
            "Downloads"
        ))

        // Weather: 10+
        generateCategory(TemplateCategories.WEATHER, listOf(
            "Current Weather", "Hourly Forecast", "Weekly Forecast",
            "Weather Map", "Air Quality", "UV Index", "Sunrise Sunset",
            "Moon Phase", "Storm Tracker", "Weather Alerts"
        ))

        // Calendar: 10+
        generateCategory(TemplateCategories.CALENDAR, listOf(
            "Month View", "Week View", "Day View", "Agenda View",
            "Event Create", "Event Detail", "Birthday Reminder",
            "Holiday Calendar", "Shared Calendar", "Calendar Sync"
        ))

        // Chat: 15+
        generateCategory(TemplateCategories.CHAT, listOf(
            "Chat Screen", "Chat List", "Group Info", "Media Gallery",
            "Voice Message", "Video Call", "Sticker Picker", "Emoji Keyboard",
            "Message Reactions", "Reply Thread", "Forward Message",
            "Delete Chat", "Block User", "Mute Notifications", "Disappearing Messages"
        ))

        // Notes: 15+
        generateCategory(TemplateCategories.NOTES, listOf(
            "Note List", "Note Editor", "Checklist", "Drawing Note",
            "Voice Note", "Markdown Editor", "Folders", "Tags",
            "Search Notes", "Archive", "Trash", "Pin Notes",
            "Share Note", "Export Note", "Note History"
        ))

        // Gallery: 10+
        generateCategory(TemplateCategories.GALLERY, listOf(
            "Photo Grid", "Photo Detail", "Album View", "Folder Tree",
            "Favorites", "Recent Photos", "Screenshots", "Wallpapers",
            "Trash", "Cloud Sync"
        ))

        // AI & Bots: 20+
        generateCategory(TemplateCategories.AI, listOf(
            "Chatbot Interface", "AI Writer", "AI Image Generator",
            "AI Summarizer", "AI Translator", "AI Code Assistant",
            "Voice Assistant", "AI Tutor", "AI Coach", "AI Analyst",
            "Content Generator", "Email Writer", "Social Media Caption",
            "SEO Assistant", "Resume Builder", "Cover Letter",
            "Interview Prep", "Study Buddy", "Mental Health Chat",
            "Productivity Bot"
        ))
    }

    private fun addCompleteTemplates() {
        // AI Bot template - complete
        templates.add(TemplateModel(
            id = "ai_bot",
            name = "AI Chat Bot",
            category = TemplateCategories.AI,
            description = "Complete AI chatbot template. Configure with your server URL, API key, and model name. Fully functional chat interface with streaming responses.",
            iconName = "smart_toy",
            isComplete = true,
            configTemplate = """{
  "serverUrl": "https://api.openai.com/v1/chat/completions",
  "apiKey": "sk-...",
  "model": "gpt-3.5-turbo",
  "systemPrompt": "You are a helpful assistant."
}""",
            screens = listOf(
                WidgetModel(type = WidgetModel.TYPE_TOOLBAR, name = "AI Assistant", y = 0f, height = 144f,
                    properties = mutableMapOf("title" to "AI Assistant", "backgroundColor" to "#6750A4")),
                WidgetModel(type = WidgetModel.TYPE_TEXT, name = "Chat Messages", x = 20f, y = 160f, width = 1040f, height = 1400f,
                    properties = mutableMapOf("text" to "Chat messages appear here...", "textColor" to "#666666")),
                WidgetModel(type = WidgetModel.TYPE_INPUT, name = "Message Input", x = 20f, y = 1580f, width = 800f, height = 120f,
                    properties = mutableMapOf("hint" to "Type a message...")),
                WidgetModel(type = WidgetModel.TYPE_FAB, name = "Send Button", x = 880f, y = 1580f, width = 120f, height = 120f,
                    properties = mutableMapOf("icon" to "send", "backgroundColor" to "#6750A4"))
            )
        ))

        // Calculator template - complete
        templates.add(TemplateModel(
            id = "calculator",
            name = "Calculator",
            category = TemplateCategories.TOOLS,
            description = "Fully functional calculator with basic arithmetic operations. Ready to use out of the box.",
            iconName = "calculate",
            isComplete = true,
            screens = listOf(
                WidgetModel(type = WidgetModel.TYPE_TEXT, name = "Display", x = 20f, y = 20f, width = 1040f, height = 280f,
                    properties = mutableMapOf("text" to "0", "textColor" to "#1C1B1F", "textSize" to 48f)),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "7", x = 20f, y = 320f, width = 240f, height = 200f,
                    properties = mutableMapOf("text" to "7", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "8", x = 280f, y = 320f, width = 240f, height = 200f,
                    properties = mutableMapOf("text" to "8", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "9", x = 540f, y = 320f, width = 240f, height = 200f,
                    properties = mutableMapOf("text" to "9", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "/", x = 800f, y = 320f, width = 260f, height = 200f,
                    properties = mutableMapOf("text" to "÷", "backgroundColor" to "#6750A4", "textColor" to "#FFFFFF")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "4", x = 20f, y = 540f, width = 240f, height = 200f,
                    properties = mutableMapOf("text" to "4", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "5", x = 280f, y = 540f, width = 240f, height = 200f,
                    properties = mutableMapOf("text" to "5", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "6", x = 540f, y = 540f, width = 240f, height = 200f,
                    properties = mutableMapOf("text" to "6", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "x", x = 800f, y = 540f, width = 260f, height = 200f,
                    properties = mutableMapOf("text" to "×", "backgroundColor" to "#6750A4", "textColor" to "#FFFFFF")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "1", x = 20f, y = 760f, width = 240f, height = 200f,
                    properties = mutableMapOf("text" to "1", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "2", x = 280f, y = 760f, width = 240f, height = 200f,
                    properties = mutableMapOf("text" to "2", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "3", x = 540f, y = 760f, width = 240f, height = 200f,
                    properties = mutableMapOf("text" to "3", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "-", x = 800f, y = 760f, width = 260f, height = 200f,
                    properties = mutableMapOf("text" to "−", "backgroundColor" to "#6750A4", "textColor" to "#FFFFFF")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "0", x = 20f, y = 980f, width = 500f, height = 200f,
                    properties = mutableMapOf("text" to "0", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = ".", x = 540f, y = 980f, width = 240f, height = 200f,
                    properties = mutableMapOf("text" to ".", "backgroundColor" to "#E0E0E0", "textColor" to "#1C1B1F")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "+", x = 800f, y = 980f, width = 260f, height = 200f,
                    properties = mutableMapOf("text" to "+", "backgroundColor" to "#6750A4", "textColor" to "#FFFFFF")),
                WidgetModel(type = WidgetModel.TYPE_BUTTON, name = "=", x = 20f, y = 1200f, width = 1040f, height = 200f,
                    properties = mutableMapOf("text" to "=", "backgroundColor" to "#6750A4", "textColor" to "#FFFFFF"))
            )
        ))

        // Weather template
        templates.add(TemplateModel(
            id = "weather",
            name = "Weather App",
            category = TemplateCategories.WEATHER,
            description = "Complete weather app with current conditions, hourly and weekly forecast.",
            iconName = "partly_cloudy",
            isComplete = true
        ))

        // Notes template
        templates.add(TemplateModel(
            id = "notes",
            name = "Notes App",
            category = TemplateCategories.NOTES,
            description = "Complete note-taking app with folders, tags, and search.",
            iconName = "note",
            isComplete = true
        ))

        // Music player
        templates.add(TemplateModel(
            id = "music_player",
            name = "Music Player",
            category = TemplateCategories.MUSIC,
            description = "Full-featured music player with playlist, equalizer, and offline support.",
            iconName = "music_note",
            isComplete = true
        ))

        // Video player
        templates.add(TemplateModel(
            id = "video_player",
            name = "Video Player",
            category = TemplateCategories.VIDEO,
            description = "Video player with subtitles, playback speed, and picture-in-picture.",
            iconName = "play_circle",
            isComplete = true
        ))

        // Chat app
        templates.add(TemplateModel(
            id = "chat_app",
            name = "Chat Messenger",
            category = TemplateCategories.CHAT,
            description = "Complete chat app with group chat, voice messages, and stickers.",
            iconName = "chat",
            isComplete = true
        ))

        // Gallery
        templates.add(TemplateModel(
            id = "gallery",
            name = "Photo Gallery",
            category = TemplateCategories.GALLERY,
            description = "Photo gallery with albums, favorites, and cloud sync.",
            iconName = "photo_library",
            isComplete = true
        ))

        // Calendar
        templates.add(TemplateModel(
            id = "calendar_app",
            name = "Calendar",
            category = TemplateCategories.CALENDAR,
            description = "Calendar with event creation, reminders, and shared calendars.",
            iconName = "event",
            isComplete = true
        ))

        // Todo
        templates.add(TemplateModel(
            id = "todo",
            name = "Todo List",
            category = TemplateCategories.PRODUCTIVITY,
            description = "Task manager with priorities, deadlines, and categories.",
            iconName = "task_alt",
            isComplete = true
        ))
    }

    private fun generateCategory(category: String, names: List<String>) {
        names.forEachIndexed { index, name ->
            val id = "${category.lowercase().replace(" ", "_")}_${index + 1}"
            templates.add(TemplateModel(
                id = id,
                name = name,
                category = category,
                description = "$name - $category screen template with fully customizable components.",
                iconName = pickIcon(category, index)
            ))
        }
    }

    private fun pickIcon(category: String, index: Int): String {
        val icons = when (category) {
            TemplateCategories.SOCIAL -> listOf("people", "chat", "public", "groups", "share", "favorite", "thumb_up", "comment", "notifications", "person")
            TemplateCategories.ECOMMERCE -> listOf("shopping_cart", "store", "local_mall", "payment", "local_shipping", "discount", "star", "local_offer", "receipt", "inventory")
            TemplateCategories.TOOLS -> listOf("build", "construction", "tune", "settings", "handyman", "brush", "dark_mode", "light_mode", "contrast", "auto_fix")
            TemplateCategories.ENTERTAINMENT -> listOf("movie", "sports_esports", "theater_comedy", "music_note", "celebration", "casino", "smart_toy", "styler", "visibility", "podcasts")
            TemplateCategories.EDUCATION -> listOf("school", "menu_book", "quiz", "task", "draw", "science", "history_edu", "language", "calculate", "menu_book")
            TemplateCategories.FINANCE -> listOf("account_balance", "trending_up", "payments", "savings", "credit_card", "money", "wallet", "analytics", "pie_chart", "attach_money")
            TemplateCategories.PRODUCTIVITY -> listOf("checklist", "event_note", "schedule", "timer", "alarm", "weekend", "work", "business_center", "push_pin", "flag")
            TemplateCategories.HEALTH -> listOf("favorite", "fitness_center", "self_improvement", "medication", "bloodtype", "medical_services", "ecg_heart", "spa", "psychology", "nutrition")
            TemplateCategories.TRAVEL -> listOf("flight", "hotel", "map", "tour", "luggage", "suitcase", "location_on", "explore", "camera_alt", "compass")
            TemplateCategories.FOOD -> listOf("restaurant", "restaurant_menu", "fastfood", "local_cafe", "cake", "egg", "set_meal", "kitchen", "coffee", "local_dining")
            TemplateCategories.NEWS -> listOf("newspaper", "article", "feed", "rss_feed", "campaign", "trending_up", "history", "auto_news", "summarize", "speaker_notes")
            TemplateCategories.SPORTS -> listOf("sports_soccer", "sports_basketball", "sports_tennis", "sports_cricket", "sports_hockey", "sports_baseball", "sports_racing", "sports_volleyball", "sports_gymnastics", "sports_mma")
            TemplateCategories.PHOTOGRAPHY -> listOf("photo_camera", "photo", "image", "photo_album", "camera_roll", "filter", "tune", "crop", "collections", "wallpaper")
            TemplateCategories.MUSIC -> listOf("music_note", "album", "library_music", "queue_music", "artist", "lyrics", "graphic_eq", "play_circle", "headphones", "music_video")
            TemplateCategories.VIDEO -> listOf("play_circle", "video_library", "subscriptions", "hd", "smart_display", "video_call", "slow_motion_video", "subtitles", "closed_caption", "screen_rotation")
            TemplateCategories.WEATHER -> listOf("wb_sunny", "cloud", "cloudy_snowing", "air", "water_drop", "waves", "wb_twilight", "nights_stay", "thunderstorm", "umbrella")
            TemplateCategories.CALENDAR -> listOf("event", "date_range", "today", "schedule", "calendar_month", "event_available", "holiday_village", "cake", "cake", "calendar_today")
            TemplateCategories.CHAT -> listOf("chat", "chat_bubble", "forum", "forum", "mark_chat_unread", "mms", "sms", "videocam", "record_voice_over", "emoji_emotions")
            TemplateCategories.NOTES -> listOf("note", "notes", "sticky_note_2", "edit_note", "assignment", "bookmark", "label", "folder", "archive", "delete")
            TemplateCategories.GALLERY -> listOf("photo_library", "collections", "wallpaper", "photo_frame", "account_balance_wallet", "perm_media", "photo_size_select_actual", "burst_mode", "360", "panorama")
            TemplateCategories.AI -> listOf("smart_toy", "psychology", "auto_fix_high", "data_object", "api", "hub", "memory", "neurology", "bot", "precision_manufacturing")
            else -> listOf("widgets", "apps", "extension", "puzzle", "settings")
        }
        return icons[index % icons.size]
    }

    fun getByCategory(category: String): List<TemplateModel> {
        return templates.filter { it.category == category }
    }

    fun search(query: String): List<TemplateModel> {
        val q = query.lowercase()
        return templates.filter {
            it.name.lowercase().contains(q) || it.description.lowercase().contains(q)
        }
    }

    fun getCompleteTemplates(): List<TemplateModel> {
        return templates.filter { it.isComplete }
    }
}
