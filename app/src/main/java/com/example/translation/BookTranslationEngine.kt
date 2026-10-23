package com.example.translation

import com.example.data.model.ParagraphPair
import com.example.data.model.VocabularyWord
import java.util.Locale

enum class TranslationStyle(val label: String, val hindiLabel: String, val description: String) {
    LITERARY("Literary Hindi", "साहित्यिक हिन्दी", "Eloquent, classic literary phrasing"),
    CONVERSATIONAL("Conversational", "सरल बोलचाल", "Natural, accessible everyday Hindi"),
    LEARNER("Learner Friendly", "शिक्षार्थी मित्र", "Clear sentences with phonetic guidance")
}

object BookTranslationEngine {

    // Common storytelling idioms and multi-word phrases mapped directly
    private val idiomPhrases = linkedMapOf(
        "once upon a time" to "एक समय की बात है",
        "long ago and far away" to "बहुत समय पहले और बहुत दूर",
        "long ago" to "बहुत समय पहले",
        "far far away" to "बहुत दूर",
        "in a faraway land" to "एक सुदूर देश में",
        "in the dead of night" to "आधी रात के सन्नाटे में",
        "at the crack of dawn" to "तड़के की पहली किरण में",
        "at first sight" to "पहली ही नज़र में",
        "in the blink of an eye" to "पलक झपकते ही",
        "out of the blue" to "अचानक बिना किसी पूर्व सूचना के",
        "deep in thought" to "गहरे विचारों में मग्न",
        "with all his might" to "अपनी पूरी ताकत से",
        "with all her heart" to "अपने पूरे दिल से",
        "from the bottom of my heart" to "मेरे दिल की गहराइयों से",
        "a matter of life and death" to "जीवन और मृत्यु का सवाल",
        "under the open sky" to "खुले आसमान के नीचे",
        "as quick as lightning" to "बिजली की तरह तेज़",
        "as clear as day" to "दिन की तरह स्पष्ट",
        "as quiet as a mouse" to "एकदम शांत",
        "as good as gold" to "सोने जैसा खरा",
        "lost in the woods" to "जंगल में भटका हुआ",
        "fell fast asleep" to "गहरी नींद में सो गया",
        "burst into tears" to "फूट-फूट कर रो पड़ा",
        "burst out laughing" to "खिलखिलाकर हंस पड़ा",
        "shook his head" to "अपना सिर हिलाया",
        "nodded her head" to "सिर हिलाकर सहमति दी",
        "took a deep breath" to "एक गहरी सांस ली",
        "came to a halt" to "अचानक रुक गया",
        "lost his temper" to "अपना आपा खो बैठा",
        "made up his mind" to "अपना मन बना लिया",
        "day and night" to "दिन-रात",
        "step by step" to "कदम-दर-कदम",
        "little by little" to "धीरे-धीरे",
        "more and more" to "और भी अधिक",
        "again and again" to "बार-बार",
        "time and again" to "बारंबार",
        "now and then" to "कभी-कभार",
        "sooner or later" to "आज नहीं तो कल",
        "face to face" to "आमने-सामने",
        "side by side" to "कंधे से कंधा मिलाकर",
        "hand in hand" to "हाथों में हाथ डाले",
        "all of a sudden" to "अचानक ही",
        "to tell the truth" to "सच कहूँ तो",
        "as a matter of fact" to "वास्तव में",
        "in other words" to "दूसरे शब्दों में",
        "on the other hand" to "दूसरी तरफ",
        "in spite of" to "के बावजूद",
        "because of" to "के कारण",
        "for the sake of" to "की खातिर",
        "in search of" to "की तलाश में",
        "in front of" to "के सामने",
        "at the end of" to "के अंत में",
        "at the beginning of" to "की शुरुआत में"
    )

    // Prepositions and Postposition mapping (English preposition -> Hindi postposition)
    private val postpositions = mapOf(
        "in" to "में",
        "inside" to "के अंदर",
        "on" to "पर",
        "upon" to "पर",
        "at" to "पर",
        "into" to "के भीतर",
        "to" to "को",
        "towards" to "की तरफ",
        "from" to "से",
        "with" to "के साथ",
        "without" to "के बिना",
        "for" to "के लिए",
        "of" to "का",
        "about" to "के बारे में",
        "under" to "के नीचे",
        "over" to "के ऊपर",
        "above" to "से ऊपर",
        "below" to "से नीचे",
        "before" to "से पहले",
        "after" to "के बाद",
        "between" to "के बीच",
        "among" to "के बीच में",
        "through" to "के माध्यम से",
        "behind" to "के पीछे",
        "beside" to "के बगल में",
        "near" to "के पास"
    )

    // Pronouns mapping
    private val pronouns = mapOf(
        "i" to "मैं",
        "me" to "मुझे",
        "my" to "मेरा",
        "mine" to "मेरा",
        "myself" to "खुद",
        "you" to "आप",
        "your" to "आपका",
        "yours" to "आपका",
        "yourself" to "अपने आप",
        "he" to "वह",
        "him" to "उसे",
        "his" to "उसका",
        "himself" to "स्वयं",
        "she" to "वह",
        "her" to "उसकी",
        "herself" to "खुद",
        "it" to "यह",
        "its" to "इसका",
        "itself" to "स्वयं",
        "we" to "हम",
        "us" to "हमें",
        "our" to "हमारा",
        "ours" to "हमारा",
        "ourselves" to "हम खुद",
        "they" to "वे",
        "them" to "उन्हें",
        "their" to "उनका",
        "theirs" to "उनका",
        "themselves" to "वे खुद",
        "this" to "यह",
        "that" to "वह",
        "these" to "ये",
        "those" to "वे",
        "who" to "कौन",
        "whom" to "किसे",
        "whose" to "किसका",
        "which" to "जो",
        "what" to "क्या",
        "where" to "कहाँ",
        "when" to "कब",
        "why" to "क्यों",
        "how" to "कैसे"
    )

    // Conjunctions & Connectives
    private val conjunctions = mapOf(
        "and" to "और",
        "but" to "लेकिन",
        "or" to "या",
        "nor" to "न ही",
        "so" to "इसलिए",
        "yet" to "फिर भी",
        "because" to "क्योंकि",
        "although" to "यद्यपि",
        "though" to "हालाँकि",
        "while" to "जब",
        "if" to "यदि",
        "unless" to "जब तक कि नहीं",
        "since" to "चूँकि",
        "therefore" to "अत:",
        "however" to "तथापि",
        "moreover" to "इसके अतिरिक्त",
        "meanwhile" to "इस बीच",
        "then" to "तब",
        "thus" to "इस प्रकार",
        "furthermore" to "इसके अलावा",
        "instead" to "इसके बजाय",
        "as" to "जैसे कि",
        "so that" to "ताकि"
    )

    // Comprehensive Literary & Storytelling Vocabulary Dictionary
    // Stores: English -> (Hindi, Transliteration, PartOfSpeech, ExampleEn, ExampleHi)
    data class DictEntry(
        val hindi: String,
        val transliteration: String,
        val partOfSpeech: String,
        val exampleEn: String,
        val exampleHi: String
    )

    private val dictionary: Map<String, DictEntry> = mapOf(
        "abandon" to DictEntry("त्यागना", "Tyagna", "Verb", "He had to abandon the quest.", "उसे खोज को त्यागना पड़ा।"),
        "adventure" to DictEntry("साहसिक यात्रा", "Sahsik Yatra", "Noun", "They embarked on a great adventure.", "वे एक महान साहसिक यात्रा पर निकले।"),
        "affection" to DictEntry("स्नेह", "Sneh", "Noun", "She looked at him with deep affection.", "उसने उसे गहरे स्नेह से देखा।"),
        "afraid" to DictEntry("भयभीत", "Bhaybhit", "Adjective", "The child was afraid of the dark.", "बच्चा अंधेरे से भयभीत था।"),
        "alone" to DictEntry("अकेला", "Akela", "Adjective", "He sat alone under the tree.", "वह पेड़ के नीचे अकेला बैठा था।"),
        "ancient" to DictEntry("प्राचीन", "Prachin", "Adjective", "An ancient castle stood on the hill.", "पहाड़ी पर एक प्राचीन किला स्थित था।"),
        "answer" to DictEntry("उत्तर", "Uttar", "Noun/Verb", "She gave no answer.", "उसने कोई उत्तर नहीं दिया।"),
        "astonished" to DictEntry("चकित", "Chakit", "Adjective", "He was astonished by her words.", "वह उसके शब्दों से चकित था।"),
        "author" to DictEntry("लेखक", "Lekhak", "Noun", "The author wrote a timeless tale.", "लेखक ने एक कालजयी कहानी लिखी।"),
        "beautiful" to DictEntry("सुंदर", "Sundar", "Adjective", "It was a beautiful morning.", "यह एक सुंदर सुबह थी।"),
        "beauty" to DictEntry("सुंदरता", "Sundarta", "Noun", "The true beauty lies within.", "सच्ची सुंदरता भीतर निहित होती है।"),
        "beginning" to DictEntry("आरंभ", "Aarambh", "Noun", "Every ending is a new beginning.", "हर अंत एक नया आरंभ होता है।"),
        "believe" to DictEntry("विश्वास करना", "Vishwas karna", "Verb", "You must believe in yourself.", "आपको खुद पर विश्वास करना होगा।"),
        "beloved" to DictEntry("प्रिय", "Priya", "Adjective", "His beloved garden blossomed.", "उसका प्रिय बगीचा खिल उठा।"),
        "bird" to DictEntry("पक्षी", "Pakshi", "Noun", "A tiny bird sang on the branch.", "डाल पर एक छोटा पक्षी गा रहा था।"),
        "blind" to DictEntry("अंधा", "Andha", "Adjective", "Love is not blind.", "प्रेम अंधा नहीं होता।"),
        "book" to DictEntry("किताब", "Kitab", "Noun", "He opened the old book carefully.", "उसने पुरानी किताब को ध्यान से खोला।"),
        "brave" to DictEntry("बहादुर", "Bahadur", "Adjective", "The brave knight stepped forward.", "बहादुर योद्धा आगे बढ़ा।"),
        "breeze" to DictEntry("मंद हवा", "Mand Hawa", "Noun", "A cool breeze swept the valley.", "घाटी में ठंडी मंद हवा बही।"),
        "bright" to DictEntry("चमकीला", "Chamkila", "Adjective", "The bright star guided them.", "चमकीले तारे ने उनका मार्गदर्शन किया।"),
        "brother" to DictEntry("भाई", "Bhai", "Noun", "The two brothers stayed together.", "दोनों भाई एक साथ रहे।"),
        "calm" to DictEntry("शांत", "Shaant", "Adjective", "The sea was perfectly calm.", "समुद्र पूरी तरह शांत था।"),
        "carefully" to DictEntry("सावधानी से", "Savdhani se", "Adverb", "He listened carefully.", "उसने सावधानी से सुना।"),
        "castle" to DictEntry("महल", "Mahal", "Noun", "The castle gates opened.", "महल के दरवाजे खुल गए।"),
        "chapter" to DictEntry("अध्याय", "Adhyay", "Noun", "Turn to the first chapter.", "पहले अध्याय पर जाएँ।"),
        "character" to DictEntry("पात्र", "Patra", "Noun", "The main character was very clever.", "मुख्य पात्र बहुत चतुर था।"),
        "child" to DictEntry("बच्चा", "Bachha", "Noun", "The little child laughed happily.", "छोटा बच्चा खुशी से हँस पड़ा।"),
        "children" to DictEntry("बच्चे", "Bachhe", "Noun", "The children played in the field.", "बच्चे मैदान में खेल रहे थे।"),
        "cloud" to DictEntry("बादल", "Baadal", "Noun", "Dark clouds covered the sky.", "काले बादलों ने आसमान ढक लिया।"),
        "courage" to DictEntry("साहस", "Saahas", "Noun", "It takes courage to speak the truth.", "सच बोलने के लिए साहस चाहिए।"),
        "crown" to DictEntry("मुकुट", "Mukut", "Noun", "The king wore a golden crown.", "राजा ने सोने का मुकुट पहना था।"),
        "curiosity" to DictEntry("जिज्ञासा", "Jigyasa", "Noun", "His curiosity knew no bounds.", "उसकी जिज्ञासा की कोई सीमा नहीं थी।"),
        "curious" to DictEntry("उत्सुक", "Utsuk", "Adjective", "Alice was very curious.", "एलिस बहुत उत्सुक थी।"),
        "darkness" to DictEntry("अंधकार", "Andhkaar", "Noun", "Light always conquers darkness.", "प्रकाश हमेशा अंधकार पर विजय पाता है।"),
        "daughter" to DictEntry("बेटी", "Beti", "Noun", "The king's daughter smiled.", "राजा की बेटी मुस्कुराई।"),
        "day" to DictEntry("दिन", "Din", "Noun", "The day was warm and peaceful.", "दिन गर्म और शांत था।"),
        "death" to DictEntry("मृत्यु", "Mrityu", "Noun", "He was not afraid of death.", "वह मृत्यु से भयभीत नहीं था।"),
        "delight" to DictEntry("आनंद", "Aanand", "Noun", "She smiled in pure delight.", "वह शुद्ध आनंद से मुस्कुराई।"),
        "desert" to DictEntry("रेगिस्तान", "Registan", "Noun", "The plane crashed in the desert.", "विमान रेगिस्तान में दुर्घटनाग्रस्त हुआ।"),
        "despair" to DictEntry("निराशा", "Nirasha", "Noun", "Do not give in to despair.", "निराशा के आगे मत झुको।"),
        "destiny" to DictEntry("भाग्य", "Bhagya", "Noun", "He believed in his destiny.", "उसे अपने भाग्य पर विश्वास था।"),
        "detective" to DictEntry("जासूस", "Jasoos", "Noun", "The brilliant detective examined the clue.", "मेधावी जासूस ने सुराग की जांच की।"),
        "discover" to DictEntry("खोज निकालना", "Khoj nikalna", "Verb", "They discovered a hidden door.", "उन्होंने एक छिपा हुआ दरवाजा खोज निकाला।"),
        "distance" to DictEntry("दूरी", "Doori", "Noun", "A voice echoed from the distance.", "दूरी से एक आवाज़ गूंजी।"),
        "door" to DictEntry("दरवाजा", "Darwaza", "Noun", "The wooden door opened slowly.", "लकड़ी का दरवाजा धीरे से खुला।"),
        "dream" to DictEntry("सपना", "Sapna", "Noun", "It felt like a wonderful dream.", "यह एक अद्भुत सपने जैसा लगा।"),
        "earth" to DictEntry("धरती", "Dharti", "Noun", "The earth was quiet and still.", "धरती शांत और स्थिर थी।"),
        "emperor" to DictEntry("सम्राट", "Samrat", "Noun", "The emperor inspected his troops.", "सम्राट ने अपनी सेना का निरीक्षण किया।"),
        "enchanted" to DictEntry("मंत्रमुग्ध", "Mantramugdha", "Adjective", "They walked into the enchanted forest.", "वे मंत्रमुग्ध जंगल में चले गए।"),
        "essential" to DictEntry("अत्यावश्यक / महत्वपूर्ण", "Atyavashyak", "Adjective", "What is essential is invisible to the eye.", "जो आवश्यक है वह आँखों से अदृश्य है।"),
        "eternal" to DictEntry("शाश्वत", "Shashwat", "Adjective", "Their friendship was eternal.", "उनकी मित्रता शाश्वत थी।"),
        "evening" to DictEntry("शाम", "Shaam", "Noun", "The cool evening settled in.", "ठंडी शाम ढलने लगी।"),
        "eyes" to DictEntry("आँखें", "Aankhein", "Noun", "Her bright eyes shone like stars.", "उसकी चमकीली आँखें तारों की तरह चमक रही थीं।"),
        "face" to DictEntry("चेहरा", "Chehra", "Noun", "A gentle smile lit his face.", "उसके चेहरे पर एक कोमल मुस्कान चमक उठी।"),
        "faith" to DictEntry("विश्वास", "Vishwas", "Noun", "Have faith in the journey.", "यात्रा पर विश्वास रखो।"),
        "father" to DictEntry("पिता", "Pita", "Noun", "His father looked at him proudly.", "उसके पिता ने गर्व से उसकी ओर देखा।"),
        "fear" to DictEntry("डर", "Dar", "Noun", "Fear faded away with the sunrise.", "सूर्योदय के साथ डर गायब हो गया।"),
        "flower" to DictEntry("फूल", "Phool", "Noun", "The rose was a delicate flower.", "गुलाब एक नाज़ुक फूल था।"),
        "forest" to DictEntry("जंगल", "Jangal", "Noun", "The animals lived deep in the forest.", "जानवर घने जंगल में रहते थे।"),
        "fortune" to DictEntry("भाग्य/धन", "Bhagya", "Noun", "Fortune favors the bold.", "भाग्य साहसी लोगों का साथ देता है।"),
        "fox" to DictEntry("लोमड़ी", "Lomdi", "Noun", "The wise fox whispered the secret.", "बुद्धिमान लोमड़ी ने रहस्य फुसफुसाया।"),
        "freedom" to DictEntry("स्वतंत्रता", "Swatantrata", "Noun", "Freedom is the greatest treasure.", "स्वतंत्रता सबसे बड़ा खजाना है।"),
        "friend" to DictEntry("मित्र", "Mitra", "Noun", "A true friend never leaves your side.", "एक सच्चा मित्र कभी आपका साथ नहीं छोड़ता।"),
        "friendship" to DictEntry("मित्रता", "Mitrata", "Noun", "Their friendship grew stronger.", "उनकी मित्रता और गहरी हो गई।"),
        "garden" to DictEntry("बगीचा", "Bagicha", "Noun", "The secret garden was full of roses.", "गुप्त बगीचा गुलाबों से भरा था।"),
        "gentle" to DictEntry("कोमल", "Komal", "Adjective", "A gentle touch calmed the animal.", "एक कोमल स्पर्श ने जानवर को शांत कर दिया।"),
        "gift" to DictEntry("उपहार", "Upahar", "Noun", "It was the most precious gift.", "यह सबसे कीमती उपहार था।"),
        "girl" to DictEntry("लड़की", "Ladki", "Noun", "The little girl skipped along.", "छोटी लड़की खुशी से कूदती हुई चली।"),
        "glory" to DictEntry("महिमा", "Mahima", "Noun", "The kingdom restored its glory.", "साम्राज्य ने अपनी महिमा वापस पाई।"),
        "gold" to DictEntry("सोना", "Sona", "Noun", "All that glitters is not gold.", "हर चमकने वाली चीज़ सोना नहीं होती।"),
        "good" to DictEntry("अच्छा", "Achha", "Adjective", "It is a good story.", "यह एक अच्छी कहानी है।"),
        "great" to DictEntry("महान", "Mahan", "Adjective", "He was a great leader.", "वह एक महान नेता था।"),
        "grief" to DictEntry("शोक", "Shok", "Noun", "Time heals all grief.", "समय हर शोक को भर देता है।"),
        "happiness" to DictEntry("खुशी", "Khushi", "Noun", "Happiness comes from giving.", "खुशी देने से मिलती है।"),
        "heart" to DictEntry("हृदय", "Hriday", "Noun", "Only with the heart can one see rightly.", "केवल हृदय से ही कोई सही देख सकता है।"),
        "hero" to DictEntry("नायक", "Nayak", "Noun", "The young hero stepped forward.", "युवा नायक आगे बढ़ा।"),
        "history" to DictEntry("इतिहास", "Itihas", "Noun", "History remembers the brave.", "इतिहास वीरों को याद रखता है।"),
        "home" to DictEntry("घर", "Ghar", "Noun", "There is no place like home.", "घर जैसी कोई जगह नहीं है।"),
        "hope" to DictEntry("आशा", "Aasha", "Noun", "Hope shines even in darkness.", "आशा अंधेरे में भी चमकती है।"),
        "horse" to DictEntry("घोड़ा", "Ghoda", "Noun", "The black horse galloped away.", "काला घोड़ा तेज़ी से दौड़ गया।"),
        "house" to DictEntry("मकान", "Makan", "Noun", "The house was warm and welcoming.", "मकान गर्मजोश और स्वागत भरा था।"),
        "important" to DictEntry("महत्वपूर्ण", "Mahatvapurna", "Adjective", "What is essential is invisible.", "जो महत्वपूर्ण है वह आँखों से अदृश्य है।"),
        "infinite" to DictEntry("अनंत", "Anant", "Adjective", "The stars spread across the infinite sky.", "तारे अनंत आकाश में फैले थे।"),
        "innocent" to DictEntry("मासूम", "Masoom", "Adjective", "The innocent smile touched his heart.", "मासूम मुस्कान ने उसके दिल को छू लिया।"),
        "island" to DictEntry("द्वीप", "Dweep", "Noun", "They landed on an uncharted island.", "वे एक अनजाने द्वीप पर उतरे।"),
        "journey" to DictEntry("यात्रा", "Yatra", "Noun", "The journey was long and tiring.", "यात्रा लंबी और थका देने वाली थी।"),
        "joy" to DictEntry("हर्ष", "Harsh", "Noun", "Tears of joy fell from her eyes.", "उसकी आँखों से हर्ष के आँसू छलक पड़े।"),
        "king" to DictEntry("राजा", "Raja", "Noun", "The king ruled with wisdom.", "राजा ने बुद्धिमत्ता से शासन किया।"),
        "kingdom" to DictEntry("राज्य", "Rajya", "Noun", "Peace reigned in the kingdom.", "राज्य में शांति का वास था।"),
        "knowledge" to DictEntry("ज्ञान", "Gyan", "Noun", "Knowledge is true power.", "ज्ञान ही वास्तविक शक्ति है।"),
        "language" to DictEntry("भाषा", "Bhasha", "Noun", "Books transcend the barriers of language.", "किताबें भाषा की सीमाओं को पार करती हैं।"),
        "laugh" to DictEntry("हँसना", "Hasna", "Verb", "He began to laugh heartily.", "वह दिल खोलकर हँसने लगा।"),
        "leader" to DictEntry("नेता", "Neta", "Noun", "A good leader listens to others.", "एक अच्छा नेता दूसरों की सुनता है।"),
        "legend" to DictEntry("दंतकथा", "Dantkatha", "Noun", "The legend was passed down generations.", "यह दंतकथा पीढ़ियों से चली आ रही थी।"),
        "life" to DictEntry("जीवन", "Jeevan", "Noun", "Life is full of surprises.", "जीवन आश्चर्यों से भरा है।"),
        "light" to DictEntry("प्रकाश", "Prakash", "Noun", "The morning light filled the room.", "सुबह का प्रकाश कमरे में भर गया।"),
        "little" to DictEntry("छोटा", "Chhota", "Adjective", "The little prince looked around.", "छोटे राजकुमार ने चारों ओर देखा।"),
        "lonely" to DictEntry("एकाकी", "Ekaki", "Adjective", "The traveler felt lonely.", "यात्री एकाकी महसूस कर रहा था।"),
        "love" to DictEntry("प्रेम", "Prem", "Noun/Verb", "Love conquers all obstacles.", "प्रेम सभी बाधाओं पर विजय पाता है।"),
        "magic" to DictEntry("जादू", "Jadoo", "Noun", "The magic word opened the cave.", "जादुई शब्द ने गुफा को खोल दिया।"),
        "man" to DictEntry("पुरुष/इंसान", "Purush", "Noun", "The wise man answered gently.", "बुद्धिमान पुरुष ने विनम्रता से उत्तर दिया।"),
        "master" to DictEntry("स्वामी", "Swami", "Noun", "He was the master of his fate.", "वह अपने भाग्य का स्वामी था।"),
        "memory" to DictEntry("स्मृति", "Smriti", "Noun", "Sweet memories lingered in his mind.", "मीठी स्मृतियाँ उसके मन में बसी रहीं।"),
        "midnight" to DictEntry("आधी रात", "Aadhi raat", "Noun", "The clock struck midnight.", "घड़ी ने आधी रात का घंटा बजाया।"),
        "mind" to DictEntry("मन", "Mann", "Noun", "A peaceful mind sees truth.", "शांत मन सत्य को देखता है।"),
        "miracle" to DictEntry("चमत्कार", "Chamatkar", "Noun", "It was nothing short of a miracle.", "यह किसी चमत्कार से कम नहीं था।"),
        "moon" to DictEntry("चाँद", "Chaand", "Noun", "The silver moon lit their path.", "चाँदी जैसे चाँद ने उनका मार्ग रोशन किया।"),
        "morning" to DictEntry("सुबह", "Subah", "Noun", "The morning dew sparkled on the grass.", "घास पर सुबह की ओस चमक रही थी।"),
        "mother" to DictEntry("माता", "Mata", "Noun", "A mother's embrace gives comfort.", "माँ का आलिंगन सुकून देता है।"),
        "mountain" to DictEntry("पहाड़", "Pahaad", "Noun", "They climbed the steep mountain.", "वे खड़े पहाड़ पर चढ़े।"),
        "mystery" to DictEntry("रहस्य", "Rahasya", "Noun", "The mystery deepened every day.", "रहस्य हर दिन और गहराता गया।"),
        "nature" to DictEntry("प्रकृति", "Prakriti", "Noun", "Nature was painted in vivid green.", "प्रकृति हरे रंगों में रंगी हुई थी।"),
        "night" to DictEntry("रात", "Raat", "Noun", "The starry night was calm.", "तारों भरी रात शांत थी।"),
        "noble" to DictEntry("महान/कुलीन", "Kulin", "Adjective", "He performed a noble deed.", "उसने एक महान कार्य किया।"),
        "ocean" to DictEntry("महासमुद्र", "Mahasamudra", "Noun", "The endless ocean met the sky.", "अनंत महासमुद्र आकाश से मिल रहा था।"),
        "old" to DictEntry("वृद्ध/पुराना", "Purana", "Adjective", "The old man smiled kindly.", "वृद्ध व्यक्ति दयालुता से मुस्कुराया।"),
        "palace" to DictEntry("राजमहल", "Rajmahal", "Noun", "Music echoed through the palace.", "राजमहल में संगीत गूंज उठा।"),
        "path" to DictEntry("मार्ग", "Maarg", "Noun", "Choose the righteous path.", "धर्म का मार्ग चुनो।"),
        "peace" to DictEntry("शांति", "Shaanti", "Noun", "Peace settled over the land.", "धरती पर शांति छा गई।"),
        "people" to DictEntry("लोग", "Log", "Noun", "The people gathered in the square.", "लोग चौराहे पर इकट्ठा हो गए।"),
        "planet" to DictEntry("ग्रह", "Grah", "Noun", "His asteroid was a tiny planet.", "उसका क्षुद्रग्रह एक नन्हा ग्रह था।"),
        "prince" to DictEntry("राजकुमार", "Rajkumar", "Noun", "The little prince asked many questions.", "छोटे राजकुमार ने कई प्रश्न पूछे।"),
        "princess" to DictEntry("राजकुमारी", "Rajkumari", "Noun", "The princess possessed great wisdom.", "राजकुमारी में अपार बुद्धिमत्ता थी।"),
        "promise" to DictEntry("वचन", "Vachan", "Noun", "A promise made is a debt unpaid.", "दिया गया वचन एक ऋण होता है।"),
        "queen" to DictEntry("रानी", "Rani", "Noun", "The queen spoke with grace.", "रानी गरिमा के साथ बोलीं।"),
        "quiet" to DictEntry("शांत", "Shaant", "Adjective", "The library was warm and quiet.", "पुस्तकालय गर्म और शांत था।"),
        "rain" to DictEntry("वर्षा", "Varsha", "Noun", "Gentle rain washed the dry earth.", "हल्की वर्षा ने सूखी धरती को सींच दिया।"),
        "read" to DictEntry("पढ़ना", "Padhna", "Verb", "He loves to read stories.", "उसे कहानियाँ पढ़ना पसंद है।"),
        "reader" to DictEntry("पाठक", "Pathak", "Noun", "A reader lives a thousand lives.", "एक पाठक हज़ार जिंदगियाँ जीता है।"),
        "remember" to DictEntry("याद रखना", "Yaad rakhna", "Verb", "Always remember who you are.", "हमेशा याद रखो कि तुम कौन हो।"),
        "rose" to DictEntry("गुलाब", "Gulaab", "Noun", "It is the time you spent on your rose.", "यह वह समय है जो तुमने अपने गुलाब पर बिताया।"),
        "secret" to DictEntry("रहस्य", "Rahasya", "Noun", "Here is my secret, it is very simple.", "यह मेरा रहस्य है, बहुत सीधा-सादा।"),
        "shadow" to DictEntry("छाया", "Chhaya", "Noun", "His shadow lengthened in the sunset.", "सूर्यास्त में उसकी छाया लंबी हो गई।"),
        "silence" to DictEntry("सन्नाटा", "Sannata", "Noun", "A profound silence filled the hall.", "सभागार में एक गहरा सन्नाटा छा गया।"),
        "silent" to DictEntry("मौन", "Maun", "Adjective", "The forest remained silent.", "जंगल मौन रहा।"),
        "sky" to DictEntry("आकाश", "Aakash", "Noun", "The azure sky was without a cloud.", "नीला आकाश बादलों से रहित था।"),
        "smile" to DictEntry("मुस्कान", "Muskan", "Noun/Verb", "Her smile lit the entire room.", "उसकी मुस्कान ने पूरा कमरा रोशन कर दिया।"),
        "soul" to DictEntry("आत्मा", "Aatma", "Noun", "A kind soul brings peace.", "एक दयालु आत्मा शांति लाती है।"),
        "sorrow" to DictEntry("दुःख", "Dukh", "Noun", "Joy and sorrow walk hand in hand.", "सुख और दुःख साथ-साथ चलते हैं।"),
        "spirit" to DictEntry("भावना/आत्मा", "Bhavna", "Noun", "Her spirit was unbroken.", "उसकी आत्मा अटूट थी।"),
        "star" to DictEntry("तारा", "Tara", "Noun", "The stars are beautiful because of a flower.", "तारे एक फूल के कारण सुंदर हैं।"),
        "stars" to DictEntry("तारे", "Taare", "Noun", "Look at the stars in the night.", "रात में तारों को देखो।"),
        "stone" to DictEntry("पत्थर", "Patthar", "Noun", "He stepped over the mossy stone.", "वह काई लगे पत्थर के ऊपर से निकला।"),
        "story" to DictEntry("कहानी", "Kahani", "Noun", "Every book tells a unique story.", "हर किताब एक अनूठी कहानी कहती है।"),
        "strength" to DictEntry("शक्ति", "Shakti", "Noun", "Strength comes from within.", "शक्ति भीतर से आती है।"),
        "sun" to DictEntry("सूर्य", "Surya", "Noun", "The golden sun rose above the horizon.", "क्षितिज के ऊपर सुनहरा सूर्य उगा।"),
        "sunset" to DictEntry("सूर्यास्त", "Suryast", "Noun", "One loves the sunset when one is sad.", "उदास होने पर सूर्यास्त प्यारा लगता है।"),
        "tame" to DictEntry("सधना/मित्र बनाना", "Sadhna", "Verb", "To me, you are still nothing more than a little boy.", "मेरे लिए, तुम अभी भी केवल एक छोटे लड़के हो।"),
        "tears" to DictEntry("आँसू", "Aansoo", "Noun", "Tears watered the dry soil.", "आँसुओं ने सूखी मिट्टी को नम किया।"),
        "time" to DictEntry("समय", "Samay", "Noun", "Time waits for no one.", "समय किसी का इंतज़ार नहीं करता।"),
        "traveler" to DictEntry("यात्री", "Yatri", "Noun", "The tired traveler rested by the well.", "थके यात्री ने कुएं के पास आराम किया।"),
        "treasure" to DictEntry("खजाना", "Khajana", "Noun", "Wisdom is the ultimate treasure.", "बुद्धिमत्ता ही परम खजाना है।"),
        "tree" to DictEntry("पेड़", "Ped", "Noun", "The old oak tree stood proudly.", "पुराना बलूत का पेड़ गर्व से खड़ा था।"),
        "truth" to DictEntry("सत्य", "Satya", "Noun", "The truth will set you free.", "सत्य आपको स्वतंत्र करेगा।"),
        "understanding" to DictEntry("समझ", "Samajh", "Noun", "Understanding requires patience.", "समझ के लिए धैर्य आवश्यक है।"),
        "valley" to DictEntry("घाटी", "Ghati", "Noun", "Wildflowers blanketed the valley.", "जंगली फूलों ने घाटी को ढक रखा था।"),
        "village" to DictEntry("गाँव", "Gaanv", "Noun", "The sleepy village woke at dawn.", "शांत गाँव भोर होते ही जाग उठा।"),
        "voice" to DictEntry("आवाज़", "Aawaaz", "Noun", "A soft voice whispered from behind.", "पीछे से एक धीमी आवाज़ फुसफुसाई।"),
        "walk" to DictEntry("चलना", "Chalna", "Verb", "They walked hand in hand.", "वे हाथों में हाथ डालकर चले।"),
        "water" to DictEntry("जल/पानी", "Jal", "Noun", "Clean water sprang from the rock.", "चट्टान से स्वच्छ जल फूट पड़ा।"),
        "whisper" to DictEntry("फुसफुसाना", "Phusphusana", "Verb", "The wind began to whisper.", "हवा फुसफुसाने लगी।"),
        "wind" to DictEntry("पवन", "Pavan", "Noun", "The cold wind rustled the dry leaves.", "ठंडी पवन ने सूखे पत्तों को सरसरा दिया।"),
        "window" to DictEntry("खिड़की", "Khidki", "Noun", "She looked out the open window.", "उसने खुली खिड़की से बाहर देखा।"),
        "wisdom" to DictEntry("बुद्धिमत्ता", "Buddhimatta", "Noun", "With age comes wisdom.", "उम्र के साथ बुद्धिमत्ता आती है।"),
        "wise" to DictEntry("बुद्धिमान", "Buddhimaan", "Adjective", "The wise owl observed quietly.", "बुद्धिमान उल्लू ने चुपचाप देखा।"),
        "woman" to DictEntry("महिला/स्त्री", "Mahila", "Noun", "To Sherlock Holmes, she is always THE woman.", "शर्लक होम्स के लिए, वह सदैव एक अद्वितीय महिला हैं।"),
        "word" to DictEntry("शब्द", "Shabd", "Noun", "A gentle word heals the heart.", "एक कोमल शब्द दिल को सुकून देता है।"),
        "words" to DictEntry("शब्द", "Shabd", "Noun", "Words hold great power.", "शब्दों में अपार शक्ति होती है।"),
        "world" to DictEntry("संसार", "Sansar", "Noun", "The world is full of wonders.", "संसार अजूबों से भरा है।"),
        "young" to DictEntry("युवा", "Yuva", "Adjective", "The young prince listened intently.", "युवा राजकुमार ने ध्यान से सुना।")
    )

    // Common verb conjugations (English -> Hindi root or past/present form)
    private val verbs = mapOf(
        "is" to "है",
        "are" to "हैं",
        "was" to "था",
        "were" to "थे",
        "am" to "हूँ",
        "has" to "पास है",
        "have" to "पास है",
        "had" to "था",
        "will" to "होगा",
        "would" to "होगा",
        "could" to "सकता था",
        "can" to "सकता है",
        "must" to "चाहिए",
        "should" to "चाहिए",
        "said" to "कहा",
        "asked" to "पूछा",
        "answered" to "उत्तर दिया",
        "replied" to "जवाब दिया",
        "cried" to "पुकारा/रोया",
        "whispered" to "फुसफुसाया",
        "shouted" to "चिल्लाया",
        "saw" to "देखा",
        "see" to "देखना",
        "looked" to "देखा",
        "went" to "गया",
        "go" to "जाना",
        "came" to "आया",
        "come" to "आना",
        "took" to "लिया",
        "take" to "लेना",
        "gave" to "दिया",
        "give" to "देना",
        "told" to "बताया",
        "tell" to "बताना",
        "knew" to "जानता था",
        "know" to "जानना",
        "thought" to "सोचा",
        "think" to "सोचना",
        "felt" to "महसूस किया",
        "feel" to "महसूस करना",
        "found" to "पाया",
        "find" to "पाना",
        "became" to "बन गया",
        "began" to "शुरू किया",
        "made" to "बनाया",
        "heard" to "सुना",
        "hear" to "सुनना",
        "stood" to "खड़ा था",
        "sat" to "बैठा",
        "lived" to "रहता था",
        "opened" to "खोला",
        "closed" to "बंद किया",
        "walked" to "चला",
        "smiled" to "मुस्कुराया"
    )

    // Lookup word in literary dictionary
    fun lookupWord(rawWord: String): VocabularyWord? {
        val clean = rawWord.trim().lowercase(Locale.ENGLISH)
            .replace(Regex("""[^\w]"""), "")

        if (clean.isBlank()) return null

        dictionary[clean]?.let { entry ->
            return VocabularyWord(
                englishWord = clean.replaceFirstChar { it.uppercase() },
                hindiMeaning = entry.hindi,
                transliteration = entry.transliteration,
                partOfSpeech = entry.partOfSpeech,
                exampleEn = entry.exampleEn,
                exampleHi = entry.exampleHi
            )
        }

        // Try stemming common suffixes: -ed, -ing, -s, -ly, -er, -est
        val candidateRoots = listOf(
            clean.removeSuffix("ing"),
            clean.removeSuffix("ed"),
            clean.removeSuffix("s"),
            clean.removeSuffix("es"),
            clean.removeSuffix("ly"),
            clean.removeSuffix("er"),
            clean.removeSuffix("est")
        ).filter { it.length >= 3 }

        for (root in candidateRoots) {
            dictionary[root]?.let { entry ->
                return VocabularyWord(
                    englishWord = clean.replaceFirstChar { it.uppercase() },
                    hindiMeaning = entry.hindi,
                    transliteration = entry.transliteration,
                    partOfSpeech = entry.partOfSpeech,
                    exampleEn = entry.exampleEn,
                    exampleHi = entry.exampleHi
                )
            }
        }

        // Check verbs list
        verbs[clean]?.let { meaning ->
            return VocabularyWord(
                englishWord = clean.replaceFirstChar { it.uppercase() },
                hindiMeaning = meaning,
                transliteration = "",
                partOfSpeech = "Verb",
                exampleEn = "He $clean.",
                exampleHi = "उसने $meaning।"
            )
        }

        // Check pronouns
        pronouns[clean]?.let { meaning ->
            return VocabularyWord(
                englishWord = clean.replaceFirstChar { it.uppercase() },
                hindiMeaning = meaning,
                transliteration = "",
                partOfSpeech = "Pronoun"
            )
        }

        return null
    }

    // Translates a single sentence with context
    fun translateSentence(sentence: String, style: TranslationStyle = TranslationStyle.LITERARY): String {
        var trimmed = sentence.trim()
        if (trimmed.isEmpty()) return ""

        // Preserve trailing punctuation (. ! ? " ”)
        val endPunct = if (trimmed.endsWith(".") || trimmed.endsWith("!") || trimmed.endsWith("?")) {
            trimmed.last().toString()
        } else ""
        if (endPunct.isNotEmpty()) {
            trimmed = trimmed.dropLast(1).trim()
        }

        var working = trimmed

        // Step 1: Match multi-word idiom phrases
        for ((phrase, translation) in idiomPhrases) {
            val pattern = Regex("(?i)\\b" + Regex.escape(phrase) + "\\b")
            if (pattern.containsMatchIn(working)) {
                working = pattern.replace(working, "[$translation]")
            }
        }

        // Step 2: Tokenize words
        val tokens = working.split(Regex("""\s+"""))
        val translatedTokens = mutableListOf<String>()

        var pendingPostposition: String? = null

        for (token in tokens) {
            if (token.startsWith("[") && token.endsWith("]")) {
                translatedTokens.add(token.removeSurrounding("[", "]"))
                continue
            }

            val clean = token.lowercase(Locale.ENGLISH).replace(Regex("""[^\w]"""), "")

            // Check if this is a preposition
            if (postpositions.containsKey(clean)) {
                pendingPostposition = postpositions[clean]
                continue
            }

            var tokenTrans = when {
                pronouns.containsKey(clean) -> pronouns[clean]!!
                conjunctions.containsKey(clean) -> conjunctions[clean]!!
                verbs.containsKey(clean) -> verbs[clean]!!
                dictionary.containsKey(clean) -> dictionary[clean]!!.hindi
                else -> {
                    // Try stemming
                    val root = listOf(
                        clean.removeSuffix("s"),
                        clean.removeSuffix("ed"),
                        clean.removeSuffix("ing"),
                        clean.removeSuffix("ly")
                    ).firstOrNull { dictionary.containsKey(it) }

                    if (root != null) dictionary[root]!!.hindi else token
                }
            }

            if (pendingPostposition != null) {
                tokenTrans = "$tokenTrans $pendingPostposition"
                pendingPostposition = null
            }

            translatedTokens.add(tokenTrans)
        }

        if (pendingPostposition != null) {
            translatedTokens.add(pendingPostposition)
        }

        // Join into natural Hindi phrase
        var hindiText = translatedTokens.joinToString(" ")

        // Clean up formatting
        hindiText = hindiText.replace("  ", " ").trim()

        // Purna Viram for Hindi sentences
        val hindiEndPunct = when (endPunct) {
            "?" -> "?"
            "!" -> "!"
            "." -> " ।"
            else -> ""
        }

        return hindiText + hindiEndPunct
    }

    // Translates an entire paragraph
    fun translateParagraph(paragraph: String, style: TranslationStyle = TranslationStyle.LITERARY): String {
        if (paragraph.isBlank()) return ""

        // Split sentences by punctuation (.!?)
        val sentences = paragraph.split(Regex("""(?<=[.!?])\s+""")).filter { it.isNotBlank() }
        if (sentences.isEmpty()) return translateSentence(paragraph, style)

        return sentences.joinToString(" ") { sentence ->
            translateSentence(sentence, style)
        }
    }

    // Translates full English text into aligned ParagraphPairs
    fun translateBookText(rawText: String, style: TranslationStyle = TranslationStyle.LITERARY): List<ParagraphPair> {
        val paras = rawText.split(Regex("""\n\s*\n"""))
            .map { it.trim() }
            .filter { it.isNotBlank() }

        return paras.mapIndexed { index, enPara ->
            val hiPara = translateParagraph(enPara, style)
            ParagraphPair(
                id = "p_$index",
                index = index,
                englishText = enPara,
                hindiText = hiPara
            )
        }
    }
}
