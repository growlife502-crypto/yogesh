package com.example.data.repository

import com.example.data.model.Book
import com.example.data.model.Chapter
import com.example.data.model.VocabularyWord

object PreloadedBooks {

    fun getInitialBooks(): List<Book> = listOf(
        Book(
            id = "book_little_prince",
            titleEn = "The Little Prince",
            titleHi = "छोटा राजकुमार",
            author = "Antoine de Saint-Exupéry",
            descriptionEn = "A poetical tale in which a young prince visits various planets in space, addressing themes of loneliness, friendship, love, and loss.",
            descriptionHi = "एक दार्शनिक और काव्यात्मक कथा, जहाँ एक नन्हा राजकुमार विभिन्न ग्रहों की यात्रा करता है और मित्रता, प्रेम व जीवन के गहरे सत्यों को समझता है।",
            category = "Classic Philosophy",
            coverColorHex = 0xFF9E4316,
            totalChapters = 3,
            currentChapterIndex = 0,
            progressPercent = 15,
            isCustom = false
        ),
        Book(
            id = "book_sherlock_holmes",
            titleEn = "Sherlock Holmes: A Scandal in Bohemia",
            titleHi = "शर्लक होम्स: बोहेमिया का किस्सा",
            author = "Arthur Conan Doyle",
            descriptionEn = "The famous opening case featuring the king of Bohemia and the unforgettable Irene Adler, the only woman to outwit Holmes.",
            descriptionHi = "शर्लक होम्स का प्रसिद्ध रहस्य, जिसमें बोहेमिया का राजा और अविस्मरणीय आइरीन एडलर शामिल हैं, जिन्होंने होम्स को मात दी थी।",
            category = "Mystery & Detective",
            coverColorHex = 0xFF27415D,
            totalChapters = 2,
            currentChapterIndex = 0,
            progressPercent = 0,
            isCustom = false
        ),
        Book(
            id = "book_jungle_book",
            titleEn = "The Jungle Book: Mowgli's Brothers",
            titleHi = "द जंगल बुक: मोगली के भाई",
            author = "Rudyard Kipling",
            descriptionEn = "The timeless story of a human child raised by wolves in the lush forests of Seeonee, guided by Baloo the bear and Bagheera the panther.",
            descriptionHi = "सियोनी की पहाड़ियों में भेड़ियों के परिवार द्वारा पाले गए मानव-शिशु मोगली की कालजयी गाथा, जिसे भालू बलू और बघीरा ने मार्गदर्शन दिया।",
            category = "Adventure & Lore",
            coverColorHex = 0xFF2D5A27,
            totalChapters = 2,
            currentChapterIndex = 0,
            progressPercent = 0,
            isCustom = false
        ),
        Book(
            id = "book_gift_of_magi",
            titleEn = "The Gift of the Magi",
            titleHi = "मेजाई का उपहार",
            author = "O. Henry",
            descriptionEn = "A touching Christmas story of selfless love and personal sacrifice between a young husband and wife.",
            descriptionHi = "एक युवा पति और पत्नी के बीच निस्वार्थ प्रेम और त्याग की अत्यंत मार्मिक और भावनात्मक कहानी।",
            category = "Short Story",
            coverColorHex = 0xFFB45309,
            totalChapters = 1,
            currentChapterIndex = 0,
            progressPercent = 0,
            isCustom = false
        ),
        Book(
            id = "book_aesop_fables",
            titleEn = "Aesop's Moral Fables",
            titleHi = "ईसप की नीति-कथाएँ",
            author = "Aesop",
            descriptionEn = "Beloved moral stories illustrating practical wisdom, patience, humility, and the nature of humankind.",
            descriptionHi = "व्यावहारिक ज्ञान, धैर्य, विनम्रता और मानव स्वभाव को दर्शाने वाली संसार की सबसे प्रसिद्ध नीति-कथाएँ।",
            category = "Fables & Wisdom",
            coverColorHex = 0xFF7C3AED,
            totalChapters = 2,
            currentChapterIndex = 0,
            progressPercent = 0,
            isCustom = false
        )
    )

    fun getInitialChapters(): List<Chapter> = listOf(
        // The Little Prince - Chapter 1
        Chapter(
            id = "little_prince_ch1",
            bookId = "book_little_prince",
            chapterIndex = 0,
            titleEn = "Chapter 1: The Boa Constrictor",
            titleHi = "अध्याय 1: अजगर और हाथी का चित्र",
            contentEn = """Once when I was six years old I saw a magnificent picture in a book, called True Stories from Nature, about the primeval forest. It was a picture of a boa constrictor in the act of swallowing an animal. Here is a copy of the drawing.

In the book it said: "Boa constrictors swallow their prey whole, without chewing it. After that they are not able to move, and they sleep through the six months that they need for digestion."

I pondered deeply, then, over the adventures of the jungle. And after some work with a colored pencil I succeeded in making my first drawing. My Drawing Number One. It looked something like this.

I showed my masterpiece to the grown-ups, and asked them whether the drawing frightened them. But they answered: "Frighten? Why should any one be frightened by a hat?"

My drawing was not a picture of a hat. It was a picture of a boa constrictor digesting an elephant. But since the grown-ups were not able to understand it, I made another drawing: I drew the inside of a boa constrictor, so that the grown-ups could see it clearly. They always need to have things explained.""",
            contentHi = """एक बार जब मैं छह वर्ष का था, मैंने आदिम जंगल के बारे में 'प्रकृति की सच्ची कहानियाँ' नामक एक पुस्तक में एक भव्य चित्र देखा। वह एक अजगर का चित्र था जो एक जंगली जानवर को निगल रहा था।

उस पुस्तक में लिखा था: "अजगर अपने शिकार को बिना चबाए पूरा निगल जाते हैं। इसके बाद वे हिलने-डुलने में असमर्थ हो जाते हैं, और पाचन के लिए आवश्यक छह महीने तक सोते रहते हैं।"

तब मैंने जंगल के रोमांचक कारनामों पर गहराई से विचार किया। रंगीन पेंसिल से कुछ देर प्रयास करने के बाद, मैं अपना पहला चित्र बनाने में सफल हुआ। मेरा चित्र क्रमांक एक।

मैंने अपनी इस उत्कृष्ट कृति को बड़ों को दिखाया, और पूछा कि क्या इस चित्र से उन्हें डर लगता है? परंतु उन्होंने उत्तर दिया: "डर? भला एक टोपी से किसी को क्यों डर लगेगा?"

मेरा चित्र किसी टोपी का नहीं था। वह तो एक हाथी को पचा रहे अजगर का चित्र था। परंतु चूँकि बड़े इसे समझने में असमर्थ थे, इसलिए मैंने एक और चित्र बनाया: मैंने अजगर के भीतरी भाग का चित्र बनाया ताकि बड़े उसे स्पष्ट देख सकें। उन्हें हमेशा हर बात समझाने की आवश्यकता होती है।"""
        ),

        // The Little Prince - Chapter 2
        Chapter(
            id = "little_prince_ch2",
            bookId = "book_little_prince",
            chapterIndex = 1,
            titleEn = "Chapter 2: The Sahara Desert",
            titleHi = "अध्याय 2: सहारा का रेगिस्तान",
            contentEn = """So I lived my life alone, without anyone that I could really talk to, until I had an accident with my airplane in the Desert of Sahara, six years ago. Something was broken in my engine. And as I had with me neither a mechanic nor any passengers, I set myself to attempt the difficult repairs all alone.

It was a question of life or death for me: I had scarcely enough drinking water to last a week. The first night, then, I went to sleep on the sand, a thousand miles from any human habitation. I was more isolated than a shipwrecked sailor on a raft in the middle of the ocean.

Thus you can imagine my amazement, at sunrise, when I was awakened by an odd little voice. It said: "If you please—draw me a sheep!"

"What!"

"Draw me a sheep..."

I jumped to my feet, completely thunderstruck. I blinked my eyes hard. I looked carefully all around me. And I saw a most extraordinary small person, who stood there examining me with great gravity.""",
            contentHi = """इस प्रकार मैं अकेला ही जीवन जीता रहा, कोई ऐसा नहीं था जिससे मैं खुलकर बात कर सकूँ, जब तक कि छह वर्ष पहले सहारा के रेगिस्तान में मेरे हवाई जहाज़ के साथ कोई दुर्घटना नहीं हुई। मेरे इंजन में कुछ टूट गया था। और चूंकि मेरे पास न तो कोई मैकेनिक था और न ही कोई यात्री, मैंने अकेले ही कठिन मरम्मत का प्रयास शुरू कर दिया।

यह मेरे लिए जीवन और मृत्यु का प्रश्न था: मेरे पास मुश्किल से एक सप्ताह तक चलने योग्य पीने का पानी था। पहली रात, मैं इंसानी बस्तियों से हज़ार मील दूर रेत पर सो गया। मैं समुद्र के बीच तख्ते पर फंसे किसी नाविक से भी अधिक अकेला था।

अतः आप सूर्योदय के समय मेरे आश्चर्य की कल्पना कर सकते हैं, जब मुझे एक विचित्र नन्ही आवाज़ ने जगा दिया। उसने कहा: "कृपया—मेरे लिए एक भेड़ का चित्र बना दो!"

"क्या!"

"मेरे लिए एक भेड़ का चित्र बनाओ..."

मैं पूरी तरह भौचक्का होकर अपने पैरों पर खड़ा हो गया। मैंने ज़ोर से अपनी आँखें मटमटाईं। मैंने अपने चारों ओर सावधानी से देखा। और मैंने एक अत्यंत असाधारण छोटे से व्यक्ति को देखा, जो बड़ी गंभीरता से मुझे निहार रहा था।"""
        ),

        // The Little Prince - Chapter 3 (The Fox)
        Chapter(
            id = "little_prince_ch3",
            bookId = "book_little_prince",
            chapterIndex = 2,
            titleEn = "Chapter 21: The Secret of the Fox",
            titleHi = "अध्याय 21: लोमड़ी का अमर रहस्य",
            contentEn = """It was then that the fox appeared.

"Good morning," said the fox.

"Good morning," the little prince replied politely, though when he turned around he saw nothing.

"I am right here," the voice said, "under the apple tree."

"Who are you?" asked the little prince, and added, "You are very pretty to look at."

"I am a fox," the fox said.

"Come and play with me," proposed the little prince. "I am so unhappy."

"I cannot play with you," the fox said. "I am not tamed."

"Ah! Please excuse me," said the little prince. But after some thought, he added: "What does that mean—'tame'?"

"It is an act too often neglected," said the fox. "It means to establish ties."

"To establish ties?"

"Just that," said the fox. "To me, you are still nothing more than a little boy who is just like a hundred thousand other little boys. And I have no need of you. And you, on your part, have no need of me. To you I am nothing more than a fox like a hundred thousand other foxes. But if you tame me, then we shall need each other. To me, you will be unique in all the world. To you, I shall be unique in all the world..."

"And now here is my secret, a very simple secret: It is only with the heart that one can see rightly; what is essential is invisible to the eye." """,
            contentHi = """तभी वहाँ लोमड़ी प्रकट हुई।

"सुप्रभात," लोमड़ी ने कहा।

"सुप्रभात," छोटे राजकुमार ने विनम्रता से उत्तर दिया, हालांकि जब वह मुड़ा तो उसे कुछ दिखाई नहीं दिया।

"मैं यहीं हूँ," आवाज़ आई, "सेब के पेड़ के नीचे।"

"तुम कौन हो?" छोटे राजकुमार ने पूछा, और जोड़ा, "तुम देखने में बहुत सुंदर हो।"

"मैं एक लोमड़ी हूँ," लोमड़ी ने कहा।

"आओ और मेरे साथ खेलो," छोटे राजकुमार ने प्रस्ताव रखा। "मैं बहुत दुखी हूँ।"

"मैं तुम्हारे साथ नहीं खेल सकती," लोमड़ी ने कहा। "मैं सधी हुई नहीं हूँ।"

"ओह! मुझे क्षमा करें," छोटे राजकुमार ने कहा। परंतु कुछ विचार करने के बाद उसने पूछा: "इसका क्या अर्थ है—'साधना'?"

"यह एक ऐसा कार्य है जिसे अक्सर भुला दिया जाता है," लोमड़ी ने कहा। "इसका अर्थ है आत्मीय संबंध स्थापित करना।"

"संबंध स्थापित करना?"

"बिल्कुल यही," लोमड़ी बोली। "मेरे लिए, तुम अभी भी केवल एक छोटे लड़के हो जो लाख अन्य छोटे लड़कों जैसा ही है। और मुझे तुम्हारी कोई आवश्यकता नहीं है। और तुम्हारी ओर से, तुम्हें मेरी कोई आवश्यकता नहीं है। तुम्हारे लिए मैं लाख अन्य लोमड़ियों जैसी ही एक लोमड़ी हूँ। परंतु यदि तुम मुझे साध लेते हो, तो हमें एक-दूसरे की आवश्यकता होगी। मेरे लिए, तुम पूरे संसार में अद्वितीय हो जाओगे। तुम्हारे लिए, मैं पूरे संसार में अद्वितीय हो जाऊँगी..."

"और अब यह रहा मेरा रहस्य, एक बहुत ही सीधा-सादा रहस्य: केवल हृदय से ही कोई सही देख सकता है; जो वास्तव में महत्वपूर्ण है वह आँखों से अदृश्य है।" """
        ),

        // Sherlock Holmes - Chapter 1
        Chapter(
            id = "sherlock_ch1",
            bookId = "book_sherlock_holmes",
            chapterIndex = 0,
            titleEn = "Chapter 1: The Woman",
            titleHi = "अध्याय 1: अद्वितीय महिला",
            contentEn = """To Sherlock Holmes she is always THE woman. I have seldom heard him mention her under any other name. In his eyes she eclipses and predominates the whole of her sex. It was not that he felt any emotion akin to love for Irene Adler. All emotions, and that one particularly, were abhorrent to his cold, precise but admirably balanced mind.

He was, I take it, the most perfect reasoning and observing machine that the world has seen, but as a lover he would have placed himself in a false position. He never spoke of the softer passions, save with a gibe and a sneer.

I had seen little of Holmes lately. My marriage had drifted us away from each other. My own complete happiness, and the home-centred interests which rise up around the man who first finds himself master of his own establishment, were sufficient to absorb all my attention; while Holmes, who loathed every form of society with his whole Bohemian soul, remained in our lodgings in Baker Street, alternating from week to week between cocaine and ambition, the drowsy drug and the fierce energy of his own keen nature.""",
            contentHi = """शर्लक होम्स के लिए वह सदैव 'अद्वितीय महिला' रहीं। मैंने शायद ही कभी उन्हें किसी अन्य नाम से पुकारते सुना हो। उनकी दृष्टि में वह अपनी संपूर्ण जाति पर भारी पड़ती थीं और सर्वोपरि थीं। ऐसा नहीं था कि वे आइरीन एडलर के प्रति प्रेम जैसी किसी भावना का अनुभव करते थे। सभी भावनाएँ, और विशेषकर यह एक, उनके शांत, सटीक और अद्भुत रूप से संतुलित मस्तिष्क के लिए सर्वथा प्रतिकूल थीं।

वे, जैसा कि मैं समझता हूँ, संसार की सबसे उत्तम तर्क और अवलोकन मशीन थे, परंतु एक प्रेमी के रूप में उन्होंने स्वयं को एक गलत स्थिति में डाल दिया होता। वे कोमल भावनाओं की चर्चा कभी नहीं करते थे, सिवाय व्यंग्य और उपहास के।

हाल ही में मेरी होम्स से कम ही मुलाकात हुई थी। मेरे विवाह ने हमें एक-दूसरे से कुछ दूर कर दिया था। मेरी अपनी संपूर्ण प्रसन्नता, और घर-गृहस्थी से जुड़े वे हित जो उस व्यक्ति के इर्द-गिर्द उभरते हैं जो पहली बार अपने घर का स्वामी बनता है, मेरे पूरे ध्यान को खींचने के लिए पर्याप्त थे; जबकि होम्स, जो अपनी पूरी बोहेमियन आत्मा से सामाजिक मेलजोल से घृणा करते थे, बेकर स्ट्रीट के हमारे आवास में ही रहे, हफ़्ते-दर-हफ़्ते सुस्ती और अपनी तीव्र प्रकृति की प्रचंड ऊर्जा के बीच झूलते हुए।"""
        ),

        // The Jungle Book - Chapter 1
        Chapter(
            id = "jungle_book_ch1",
            bookId = "book_jungle_book",
            chapterIndex = 0,
            titleEn = "Chapter 1: Mowgli's Brothers",
            titleHi = "अध्याय 1: मोगली के भाई",
            contentEn = """It was seven o'clock of a very warm evening in the Seeonee hills when Father Wolf woke up from his day's rest, scratched himself, yawned, and spread out his paws one after the other to get rid of the sleepy feeling in their tips. Mother Wolf lay with her big gray nose dropped across her four tumbling, squealing cubs, and the moon shone into the mouth of the cave where they all lived.

"Augrh!" said Father Wolf. "It is time to hunt again."

He was going to spring down hill when a little shadow with a bushy tail crossed the threshold and whined: "Good luck go with you, O Chief of the Wolves. And good luck and strong white teeth go with noble children that they may never forget the hungry in this world."

It was the jackal—Tabaqui, the Dish-licker—and the wolves of India despise Tabaqui because he runs about making mischief, and telling tales, and eating rags and pieces of leather from the village rubbish-heaps.""",
            contentHi = """सियोनी की पहाड़ियों में एक बहुत ही गर्म शाम के सात बजे थे, जब पिता भेड़िया दिन भर के आराम के बाद जागे, खुद को खुजलाया, जंभाई ली, और अपने पंजों को एक के बाद एक फैलाया ताकि उनके सिरों में भरा आलस्य दूर हो सके। माँ भेड़िया अपनी बड़ी भूरी नाक को अपने चार उछलते-कूदते, किकियाते बच्चों पर टिकाए लेटी हुई थीं, और चाँद की रोशनी उस गुफा के मुहाने पर चमक रही थी जहाँ वे सब रहते थे।

"औग्र्ह!" पिता भेड़िया बोले। "फिर से शिकार का समय हो गया है।"

वे पहाड़ी से नीचे कूदने ही वाले थे कि एक झाड़ीदार पूंछ वाली छोटी सी छाया ने गुफा की चौखट पार की और गिड़गिड़ाई: "भेड़ियों के सरदार, आपका भाग्य आपका साथ दे। और आपके कुलीन बच्चों के पास मजबूत सफेद दांत हों ताकि वे इस दुनिया के भूखों को कभी न भूलें।"

वह सियार था—तबाकी, जूठन चाटने वाला—और भारत के भेड़िए तबाकी से घृणा करते हैं क्योंकि वह उपद्रव मचाता फिरता है, चुगलियाँ करता है, और गाँव के कूड़े के ढेरों से फटे-पुराने कपड़े और चमड़े के टुकड़े खाता रहता है।"""
        ),

        // The Gift of the Magi - Chapter 1
        Chapter(
            id = "gift_magi_ch1",
            bookId = "book_gift_of_magi",
            chapterIndex = 0,
            titleEn = "The Gift of the Magi",
            titleHi = "मेजाई का उपहार (पूर्ण कहानी)",
            contentEn = """One dollar and eighty-seven cents. That was all. And sixty cents of it was in pennies. Pennies saved one and two at a time by bulldozing the grocer and the vegetable man and the butcher until one's cheeks burned with the silent imputation of parsimony that such close dealing implied. Three times Della counted it. One dollar and eighty-seven cents. And the next day would be Christmas.

There was clearly nothing to do but flop down on the shabby little couch and howl. So Della did it. Which instigates the moral reflection that life is made up of sobs, sniffles, and smiles, with sniffles predominating.

While the mistress of the home is gradually subsiding from the first stage to the second, take a look at the home. A furnished flat at eight dollars per week. It did not exactly beggar description, but it certainly had that word on the lookout for the mendicancy squad.

Now, there were two possessions of the James Dillingham Youngs in which they both took a mighty pride. One was Jim's gold watch that had been his father's and his grandfather's. The other was Della's hair.""",
            contentHi = """एक डॉलर और सत्तासी सेंट। कुल जमा यही था। और इसमें से साठ सेंट तो एक-एक पैसे के सिक्कों में थे। किराने वाले, सब्ज़ी वाले और कसाई से एक-एक दो-दो पैसे की इतनी चिक-चिक करके बचाए गए थे कि कंजूसी के मौन आरोप से गाल लाल हो उठते थे। डेला ने इसे तीन बार गिना। एक डॉलर और सत्तासी सेंट। और अगला ही दिन क्रिसमस का था।

साफ था कि उस फटे-पुराने छोटे से सोफे पर गिरकर रोने के सिवा कुछ और करने को नहीं था। सो डेला ने वही किया। जो इस नैतिक विचार को जन्म देता है कि जीवन सिसकियों, सुबकियों और मुस्कानों से मिलकर बना है, जिनमें सुबकियों का ही बोलबाला रहता है।

जब घर की मालकिन धीरे-धीरे पहले चरण से शांत होकर दूसरे चरण पर आ रही है, तब तक ज़रा घर पर एक नज़र डाल लीजिए। आठ डॉलर प्रति सप्ताह पर सुसज्जित एक छोटा फ्लैट।

अब, जेम्स डिलिंघम यंग दंपत्ति के पास ऐसी दो अनमोल वस्तुएं थीं जिन पर वे दोनों अत्यधिक गर्व करते थे। एक थी जिम की सोने की घड़ी जो उसके पिता और दादा की धरोहर थी। दूसरी चीज़ थी डेला के लंबे, रेशमी बाल।"""
        ),

        // Aesop's Fables - Chapter 1
        Chapter(
            id = "aesop_ch1",
            bookId = "book_aesop_fables",
            chapterIndex = 0,
            titleEn = "The Tortoise and the Hare",
            titleHi = "कछुआ और खरगोश",
            contentEn = """A Hare was making fun of the Tortoise one day for being so slow.

"Do you ever get anywhere?" he asked with a mocking laugh.

"Yes," replied the Tortoise, "and I get there sooner than you think. Run with me and I will prove it."

The Hare was much amused at the idea of running a race with the Tortoise, but for the fun of the thing he agreed. So the Fox, who had consented to act as judge, marked the distance and started the runners off.

The Hare was soon far out of sight, and to make the Tortoise feel very deeply how ridiculous it was for him to try a race with a Hare, he lay down beside the course to take a nap until the Tortoise should catch up.

The Tortoise meanwhile kept going slowly but steadily, and, after a time, passed the place where the Hare was sleeping. But the Hare slept on very peacefully; and when at last he did wake up, the Tortoise was near the goal. The Hare now ran his swiftest, but he could not overtake the Tortoise in time.

Slow and steady wins the race.""",
            contentHi = """एक दिन एक खरगोश एक कछुए की धीमी गति का मज़ाक उड़ा रहा था।

"क्या तुम कभी कहीं पहुँच भी पाते हो?" उसने उपहास भरी हँसी के साथ पूछा।

"हाँ," कछुए ने उत्तर दिया, "और मैं तुम्हारी सोच से भी पहले वहाँ पहुँच जाता हूँ। मेरे साथ दौड़ लगाओ और मैं इसे साबित कर दूँगा।"

कछुए के साथ दौड़ लगाने के विचार पर खरगोश बहुत खुश हुआ, परंतु केवल मनोरंजन की खातिर वह सहमत हो गया। अतः लोमड़ी, जो निर्णायक बनने के लिए सहमत हुई थी, ने दूरी तय की और धावकों को रवाना किया।

खरगोश जल्द ही बहुत आगे निकलकर आँखों से ओझल हो गया, और कछुए को यह गहराई से महसूस कराने के लिए कि खरगोश के साथ दौड़ना कितना हास्यास्पद था, वह रास्ते के किनारे एक झपकी लेने के लिए लेट गया ताकि कछुआ उसके पास तक पहुँच सके।

इस बीच कछुआ धीरे-धीरे परंतु निरंतर चलता रहा, और कुछ समय बाद, उस स्थान से आगे निकल गया जहाँ खरगोश सो रहा था। परंतु खरगोश बड़ी शांति से सोता रहा; और जब वह अंततः जागा, तो कछुआ विजय रेखा के बिल्कुल करीब था। खरगोश अब अपनी पूरी गति से दौड़ा, परंतु वह समय रहते कछुए से आगे नहीं निकल सका।

धैर्य और निरंतरता से ही सफलता प्राप्त होती है।"""
        )
    )

    fun getInitialVocabulary(): List<VocabularyWord> = listOf(
        VocabularyWord(
            englishWord = "Essential",
            hindiMeaning = "अत्यावश्यक / महत्वपूर्ण",
            transliteration = "Atyavashyak",
            partOfSpeech = "Adjective",
            exampleEn = "What is essential is invisible to the eye.",
            exampleHi = "जो आवश्यक है वह आँखों से अदृश्य है।",
            sourceBook = "The Little Prince",
            isFavorite = true
        ),
        VocabularyWord(
            englishWord = "Tame",
            hindiMeaning = "साधना / मित्र बनाना",
            transliteration = "Sadhna",
            partOfSpeech = "Verb",
            exampleEn = "To me, you will be unique in all the world.",
            exampleHi = "मेरे लिए, तुम पूरे संसार में अद्वितीय हो जाओगे।",
            sourceBook = "The Little Prince",
            isFavorite = true
        ),
        VocabularyWord(
            englishWord = "Eclipses",
            hindiMeaning = "फीका कर देना / ग्रहण लगाना",
            transliteration = "Feeka kar dena",
            partOfSpeech = "Verb",
            exampleEn = "In his eyes she eclipses the whole of her sex.",
            exampleHi = "उनकी दृष्टि में वह अपनी संपूर्ण जाति पर भारी पड़ती थीं।",
            sourceBook = "Sherlock Holmes",
            isFavorite = false
        ),
        VocabularyWord(
            englishWord = "Abhorrent",
            hindiMeaning = "घृणास्पद / सर्वथा प्रतिकूल",
            transliteration = "Ghrinaspad",
            partOfSpeech = "Adjective",
            exampleEn = "All emotions were abhorrent to his cold mind.",
            exampleHi = "सभी भावनाएँ उनके शांत मस्तिष्क के लिए प्रतिकूल थीं।",
            sourceBook = "Sherlock Holmes",
            isFavorite = false
        ),
        VocabularyWord(
            englishWord = "Threshold",
            hindiMeaning = "चौखट / देहरी",
            transliteration = "Chaukhat",
            partOfSpeech = "Noun",
            exampleEn = "A shadow crossed the threshold of the cave.",
            exampleHi = "एक छाया ने गुफा की चौखट पार की।",
            sourceBook = "The Jungle Book",
            isFavorite = false
        ),
        VocabularyWord(
            englishWord = "Parsimony",
            hindiMeaning = "कंजूसी / मितव्ययिता",
            transliteration = "Kanjusi",
            partOfSpeech = "Noun",
            exampleEn = "Her cheeks burned with the imputation of parsimony.",
            exampleHi = "कंजूसी के मौन आरोप से उसके गाल लाल हो उठे।",
            sourceBook = "The Gift of the Magi",
            isFavorite = true
        )
    )
}
