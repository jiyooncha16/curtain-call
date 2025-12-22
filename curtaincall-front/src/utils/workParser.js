export function parseActorWorks(actor) {
  const today = new Date()
  today.setHours(0, 0, 0, 0)

  const periodRegex =
    /(\d{4})\.(\d{2})\.(\d{2})\s*~\s*(\d{4})\.(\d{2})\.(\d{2})/

  const works = Object.keys(actor)
    .filter(k => k.startsWith('work'))
    .map((k, idx) => {
      const raw = actor[k]
      if (!raw || !raw.trim()) return null

      // 기간 파싱
      const m = raw.match(periodRegex)
      const period = m ? m[0] : ''

      let start = null
      let end = null
      if (m) {
        start = new Date(Number(m[1]), Number(m[2]) - 1, Number(m[3]))
        end = new Date(Number(m[4]), Number(m[5]) - 1, Number(m[6]))
        start.setHours(0, 0, 0, 0)
        end.setHours(0, 0, 0, 0)
      }

      // 제목
      const title = m ? raw.slice(0, m.index).trim() : raw.trim()

      // 기간 뒤 텍스트
      const afterPeriod = m
        ? raw.slice(m.index + period.length).trim()
        : ''

      // 배역
      const roleMatch = afterPeriod.match(/[^ \n]+역/)
      const role = roleMatch ? roleMatch[0] : ''

      // 극장
      const theater = role
        ? afterPeriod.replace(role, '').trim()
        : afterPeriod

      // 현재 공연 여부 + D-day
      const isCurrent = !!(start && end && today >= start && today <= end)

      let dDay = null
      if (isCurrent && end) {
        dDay = Math.ceil((end - today) / (1000 * 60 * 60 * 24))
      }

      return {
        id: idx + 1,
        title,
        period,
        theater,
        role,
        raw,
        startDate: start,
        endDate: end,
        isCurrent,
        dDay,
      }
    })
    .filter(Boolean)

  /*  정렬 */
  works.sort((a, b) => {
    // 1. 현재 공연 우선
    if (a.isCurrent !== b.isCurrent) {
      return a.isCurrent ? -1 : 1
    }

    // 2. 종료일 빠른 순
    if (a.isCurrent && b.isCurrent) {
      return a.endDate - b.endDate
    }

    // 3. 그 외는 최신순
    return b.startDate - a.startDate
  })

  return works
}
