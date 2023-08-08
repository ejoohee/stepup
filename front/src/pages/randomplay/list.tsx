import {useEffect, useState} from "react"

import Header from "components/Header"
import MainBanner from "components/MainBanner"
import Footer from "components/Footer"
import LanguageButton from "components/LanguageButton"

import Link from "next/link"

import Image from "next/image"
import ArticleIcon from "/public/images/article-icon.svg"
import RandomplayThumbnail from "/public/images/randomplay-thumbnail.png"
import TopIcon from "/public/images/icon-top.svg"
import NowIcon from "/public/images/icon-now.svg"
import SoonIcon from "/public/images/icon-soon.svg"

import { accessTokenState, refreshTokenState, idState, nicknameState } from "states/states";
import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

import { axiosDance, axiosUser } from "apis/axios"

const RandomPlayList = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const [rooms, setRooms] = useState<any[]>();
    const [inprogress, setInprogress] = useState<any[]>();
    const [scheduled, setScheduled] = useState<any[]>();

    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [nickname, setNickname] = useRecoilState(nicknameState);
    useEffect(() => {
        axiosDance.get('',{
            params: {
                progressType: "ALL",
            },
        }).then((data) => {
            console.log(data);
            if(data.data.message === "참여 가능한 랜덤 플레이 댄스 목록 조회 완료"){
                setRooms(data.data.data);
            }
        })

        axiosDance.get('',{
            params: {
                progressType: "IN_PROGRESS",
            }
        }).then((data) => {
            if(data.data.message === "진행 중인 랜덤 플레이 댄스 목록 조회 완료"){
                setInprogress(data.data.data);
            }
        })

        axiosDance.get('',{
            params: {
                progressType: "SCHEDULED",
            }
        }).then((data) => {
            if(data.data.message === "진행 예정된 랜덤 플레이 댄스 목록 조회 완료"){
                setScheduled(data.data.data);
            }
        })
    },[]);
    return(
        <>
            <Header/>
            <MainBanner/>
            <div className="randomplay-list-wrap">
                <section id="popular">
                    <div className="section-title">
                        <div className="flex-wrap">
                            <Image src={ArticleIcon} alt=""/>
                            <h3>{lang==="en" ? "Popular right now" : lang==="cn" ? "现在很有人气的随机舞蹈" : "현재 인기 있는 랜덤 플레이 댄스" }</h3>
                        </div>
                         <button><Link href="/randomplay/create">{lang==="en" ? "Holding" : lang==="cn" ? "打开" : "개최하기" }</Link></button>
                    </div>
                    <div className="section-content">
                        <ul>
                            {rooms?.map((room, index) => {
                                return(
                                    <li key={index}>
                                        <Link href={{
                                            pathname: `/danceroom/${room.randomDanceId}`,
                                            query:{
                                                title: room.title,
                                                content: room.content,
                                                startAt: room.startAt,
                                                endAt: room.endAt,
                                                myName: nickname,
                                            },
                                        }}>
                                            <div className="section-content-img">
                                                <span>{room.danceType === "SURVIVAL" ? "서바이벌" : room.danceType === "BASIC" ? "자유모드" : "랜플댄모드"}</span>
                                                <Image src={RandomplayThumbnail} alt=""/>
                                            </div>
                                            <div className="section-content-info">
                                                <h4>{room.title}</h4>
                                                <span>{room.hostNickname}</span>
                                                <div className="flex-wrap">
                                                    <button>예약하기</button>
                                                    <span>참여 PM6시 ~ PM7시</span>
                                                </div>
                                            </div>
                                        </Link>
                                    </li>
                                )
                            })}
                        </ul>
                    </div>
                    <div className="more-button-wrap">
                        <button>{lang==="en" ? "More Details" : lang==="cn" ? "查看更多" : "더보기" }</button>
                    </div>
                </section>
                <section id="now">
                    <div className="section-title">
                        <div className="flex-wrap">
                            <Image src={ArticleIcon} alt=""/>
                            <h3>{lang==="en" ? "Going on right now" : lang==="cn" ? "正在进行的随机舞蹈" : "현재 진행 중인 랜덤 플레이 댄스" }</h3>
                        </div>
                    </div>
                    <div className="section-content">
                        <ul>
                            {inprogress?.map((inprogress, index) => {
                                return(
                                    <li key={index}>
                                        <Link href="/danceroom">
                                            <div className="section-content-img">
                                                <span>{inprogress.danceType === "SURVIVAL" ? "서바이벌" : inprogress.danceType === "BASIC" ? "자유모드" : "랜플댄모드"}</span>
                                                <Image src={RandomplayThumbnail} alt=""/>
                                            </div>
                                            <div className="section-content-info">
                                                <h4>{inprogress.title}</h4>
                                                <span>{inprogress.hostNickname}</span>
                                                <div className="flex-wrap">
                                                    <button>예약하기</button>
                                                    <span>참여 PM6시 ~ PM7시</span>
                                                </div>
                                            </div>
                                        </Link>
                                    </li>
                                )
                            })}
                        </ul>
                    </div>
                    <div className="more-button-wrap">
                        <button>{lang==="en" ? "More Details" : lang==="cn" ? "查看更多" : "더보기" }</button>
                    </div>
                </section>
                <section id="soon">
                    <div className="section-title">
                        <div className="flex-wrap">
                            <Image src={ArticleIcon} alt=""/>
                            <h3>{lang==="en" ? "Dance scheduled for today" : lang==="cn" ? "预定的随机舞蹈" : "진행 예정된 랜덤 플레이 댄스" }</h3>
                        </div>
                    </div>
                    <div className="section-content">
                        <ul>
                            {
                                scheduled?.map((scheduled, index) => {
                                    return(
                                        <li key={index}>
                                            <Link href="/danceroom">
                                                <div className="section-content-img">
                                                    <span>{scheduled.danceType === "SURVIVAL" ? "서바이벌" : scheduled.danceType === "BASIC" ? "자유모드" : "랜플댄모드"}</span>
                                                    <Image src={RandomplayThumbnail} alt=""/>
                                                </div>
                                                <div className="section-content-info">
                                                    <h4>{scheduled.title}</h4>
                                                    <span>{scheduled.hostNickname}</span>
                                                    <div className="flex-wrap">
                                                        <button>예약하기</button>
                                                        <span>참여 PM6시 ~ PM7시</span>
                                                    </div>
                                                </div>
                                            </Link>
                                        </li>
                                    )
                                })
                            }
                        </ul>
                    </div>
                    <div className="more-button-wrap">
                        <button>{lang==="en" ? "More Details" : lang==="cn" ? "查看更多" : "더보기" }</button>
                    </div>
                </section>
                <div className="floating-box">
                    <ul>
                        <li>
                            <a href="#popular">
                                <Image src={TopIcon} alt=""/>
                                <span>{lang==="en" ? "Popular" : lang==="cn" ? "人气" : "인기 랜플댄" }</span>
                            </a>
                        </li>
                        <li>
                            <a href="#now">
                                <Image src={NowIcon} alt=""/>
                                <span>{lang==="en" ? "In progress" : lang==="cn" ? "进行中" : "진행중인 랜플댄" }</span>
                            </a>
                        </li>
                        <li>
                            <a href="#soon">
                                <Image src={SoonIcon} alt=""/>
                                <span>{lang==="en" ? "Scheduled to proceed" : lang==="cn" ? "预定进行" : "진행예정 랜플댄" }</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <LanguageButton/>
            <Footer/>
        </>
    )
}

export default RandomPlayList