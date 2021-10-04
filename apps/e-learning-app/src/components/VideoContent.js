import VideosRow from 'VideosRow'
import Sidebar from 'Sidebar'
const VideoContent = () => {
    const allVideos = [
        {
            thumbnail: `Lectie1.png`,
            title: "Froda in literatura interbelica ",
            videoLink:"https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            length: 208,
            channel: {
                name: "Profesor Badea",
                link: "https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g",
                image: "https://picsum.photos/30/30"
            },
            viewCount: 1812124001,
            uploadedAt: "2020-12-01"
        },
        {
            // thumbnail: `https://p,icsum.photos/id/${Math.floor(Math.random() * 100)}/300/150`,
            thumbnail: `Lectie2.png`,
            title: "Froda in literatura interbelica",
            videoLink:"https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            length: 208,
            channel: {
                name: "Profesor Banescu",
                link: "https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g",
                image: "https://picsum.photos/30/30"
            },
            viewCount: 1812001,
            uploadedAt: "2020-12-01"
        },
        {
            // thumbnail: `https://p,icsum.photos/id/${Math.floor(Math.random() * 100)}/300/150`,
            thumbnail: `Lectie3.png`,
            title: "Froda in literatura interbelica ",
            videoLink:"https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            length: 208,
            channel: {
                name: "Profesor Isabela",
                link: "https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g",
                image: "https://picsum.photos/30/30"
            },
            viewCount: 1812124001,
            uploadedAt: "2020-12-01"
        },
        {
            // thumbnail: `https://p,icsum.photos/id/${Math.floor(Math.random() * 100)}/300/150`,
            thumbnail: `Lectie4.png`,
            title: "Ion Vinea: \'Manifest activist  catre tinerime\'",
            videoLink:"https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            length: 208,
            channel: {
                name: "Profesor Badea",
                link: "https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g",
                image: "https://picsum.photos/30/30"
            },
            viewCount: 13001,
            uploadedAt: "2020-12-01"
        },
        {
            // thumbnail: `https://p,icsum.photos/id/${Math.floor(Math.random() * 100)}/300/150`,
            thumbnail: `Lectie5.png`,
            title: "Tristea Tzara: \'Manifet despe amorul slab si amorul amar\'",
            videoLink:"https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            length: 208,
            channel: {
                name: "Profesor Badea",
                link: "https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g",
                image: "https://picsum.photos/30/30"
            },
            viewCount: 1812001,
            uploadedAt: "2020-12-01"
        },
        {
            // thumbnail: `https://p,icsum.photos/id/${Math.floor(Math.random() * 100)}/300/150`,
            thumbnail: `Lectie6.png`,
            title: "Punctuatia si justificarile ei sintactice si stilistice",
            videoLink:"https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            length: 208,
            channel: {
                name: "Profesor Badea",
                link: "https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g",
                image: "https://picsum.photos/30/30"
            },
            viewCount: 1812001,
            uploadedAt: "2020-12-01"
        },
        {
            // thumbnail: `https://p,icsum.photos/id/${Math.floor(Math.random() * 100)}/300/150`,
            thumbnail: `Lectie7.png`,
            title: "Punctiatia si justificarile ei sintactice si stilistice",
            videoLink:"https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            length: 208,
            channel: {
                name: "Profesor Badea",
                link: "https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g",
                image: "https://picsum.photos/30/30"
            },
            viewCount: 1812001,
            uploadedAt: "2020-12-01"
        },
         
         

    ]

    const marqChan = {
        name: "Marques Brownlee",
        link: "https://www.youtube.com/channel/UC9CoOnJkIBMdeijd9qYoT_g",
        image: "https://picsum.photos/30/30"
    }
    return (
    <>
        <Sidebar title="II. Studiu de caz"/>
        <VideosRow 
                type= "normal"
                label= "Recommended"
                videos= {allVideos}
        />
    </>
    )
}

export default VideoContent
